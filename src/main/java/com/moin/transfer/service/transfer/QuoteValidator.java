package com.moin.transfer.service.transfer;

import com.moin.transfer.common.constants.FeeConstants;
import com.moin.transfer.common.enums.IdType;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import com.moin.transfer.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class QuoteValidator {
    private final QuoteRepository quoteRepository;

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

    public void validateDailyLimit(User user, BigDecimal targetAmount) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        BigDecimal dailyTotal = quoteRepository.findDailyTotalAmountByUser(
                user.getId(), startOfDay, endOfDay);
        BigDecimal totalWithCurrentAmount = dailyTotal.add(targetAmount);

        BigDecimal limit = user.getIdType() == IdType.REG_NO ?
                FeeConstants.INDIVIDUAL_DAILY_LIMIT :
                FeeConstants.BUSINESS_DAILY_LIMIT;

        if (totalWithCurrentAmount.compareTo(limit) > 0) {
            throw new QuoteException(QuoteErrorCode.DAILY_LIMIT_EXCEEDED);
        }
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
}

