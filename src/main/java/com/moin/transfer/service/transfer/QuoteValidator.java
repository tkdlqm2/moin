package com.moin.transfer.service.transfer;

import com.moin.transfer.common.constants.FeeConstants;
import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.common.enums.IdType;
import com.moin.transfer.dto.response.QuoteAmount;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import com.moin.transfer.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuoteValidator {
    private static final int USD_SCALE = Math.min(12, Currency.USD.getDefaultFractionDigits());

    private final QuoteRepository quoteRepository;
    private final ExchangeRateService exchangeRateService;

    public void validateAmount(long amount) {
        if (amount <= 0) {
            throw new QuoteException(QuoteErrorCode.NEGATIVE_NUMBER);
        }
    }

    public void validateTargetAmount(BigDecimal targetAmount) {
        if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new QuoteException(QuoteErrorCode.NEGATIVE_NUMBER);
        }
    }

    public void validateDailyLimit(User user, BigDecimal targetAmount, Currency currency, BigDecimal amount) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        BigDecimal dailyTotal = calculateDailyTotal(user, startOfDay, endOfDay);
        BigDecimal totalWithCurrentAmount = calculateTotalWithNewAmount(
                dailyTotal, targetAmount, currency, amount);

        validateAgainstLimit(user, totalWithCurrentAmount);
    }

    public Quote validateAndGetQuote(long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new QuoteException(QuoteErrorCode.QUOTE_NOT_FOUND));

        if (quote.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new QuoteException(QuoteErrorCode.QUOTE_EXPIRED);
        }

        if (quote.isUsed()) {
            throw new QuoteException(QuoteErrorCode.QUOTE_ALREADY_USED);
        }

        return quote;
    }

    public BigDecimal calculateTotalAmountInUsd(List<QuoteAmount> quotes, BigDecimal usdExchangeRate) {
        if (quotes == null || quotes.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return quotes.stream()
                .map(quote -> convertToUsd(quote, usdExchangeRate))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(USD_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDailyTotal(User user, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        List<QuoteAmount> dailyQuotes = quoteRepository.findDailyQuoteAmountsByUser(
                user.getId(), startOfDay, endOfDay);
        BigDecimal usdExchangeRate = exchangeRateService.getExchangeRate(Currency.USD);

        return dailyQuotes.isEmpty() ?
                BigDecimal.ZERO :
                calculateTotalAmountInUsd(dailyQuotes, usdExchangeRate);
    }

    private BigDecimal calculateTotalWithNewAmount(
            BigDecimal dailyTotal,
            BigDecimal targetAmount,
            Currency currency,
            BigDecimal amount) {
        BigDecimal usdExchangeRate = exchangeRateService.getExchangeRate(Currency.USD);
        return dailyTotal.add(convertToUsd(targetAmount, currency, usdExchangeRate, amount));
    }

    private void validateAgainstLimit(User user, BigDecimal totalAmount) {
        BigDecimal limit = user.getIdType() == IdType.REG_NO ?
                FeeConstants.INDIVIDUAL_DAILY_LIMIT :
                FeeConstants.BUSINESS_DAILY_LIMIT;

        if (totalAmount.compareTo(limit) > 0) {
            throw new QuoteException(QuoteErrorCode.LIMIT_EXCESS);
        }
    }

    private BigDecimal convertToUsd(BigDecimal amount, Currency currency, BigDecimal usdExchangeRate) {
        if (currency == Currency.USD) {
            return amount;
        }

        if (currency == Currency.JPY) {
            return amount.divide(usdExchangeRate, USD_SCALE, RoundingMode.HALF_UP);
        }

        throw new QuoteException(QuoteErrorCode.UNSUPPORTED_CURRENCY);
    }

    private BigDecimal convertToUsd(QuoteAmount quote, BigDecimal usdExchangeRate) {
        return convertToUsd(
                quote.getTargetCurrency() == Currency.USD ? quote.getTargetAmount() : quote.getAmount(),
                quote.getTargetCurrency(),
                usdExchangeRate
        );
    }

    private BigDecimal convertToUsd(BigDecimal targetAmount, Currency currency, BigDecimal usdExchangeRate, BigDecimal amount) {
        return convertToUsd(
                currency == Currency.USD ? targetAmount : amount,
                currency,
                usdExchangeRate
        );
    }
}
