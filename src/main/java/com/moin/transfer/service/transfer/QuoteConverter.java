package com.moin.transfer.service.transfer;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.dto.response.QuoteData;
import com.moin.transfer.dto.response.TransferHistory;
import com.moin.transfer.entity.Quote;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuoteConverter {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static QuoteData toQuoteData(Quote quote) {
        return QuoteData.builder()
                .quoteId(String.valueOf(quote.getId()))
                .exchangeRate(quote.getExchangeRate())
                .expireTime(formatDateTime(quote.getExpireTime()))
                .targetAmount(quote.getTargetAmount())
                .build();
    }

    public static List<TransferHistory> toTransferHistoryList(
            List<Quote> quotes,
            BigDecimal usdExchangeRate
    ) {
        return quotes.stream()
                .map(quote -> toTransferHistory(quote, usdExchangeRate))
                .collect(Collectors.toList());
    }

    private static TransferHistory toTransferHistory(Quote quote, BigDecimal usdExchangeRate) {
        BigDecimal sourceAmountInKrw = quote.getAmount().subtract(quote.getFee());
        BigDecimal usdAmount = sourceAmountInKrw.divide(usdExchangeRate, Currency.USD.getDefaultFractionDigits(), RoundingMode.HALF_UP);

        return TransferHistory.builder()
                .sourceAmount(quote.getAmount())
                .fee(quote.getFee())
                .usdExchangeRate(usdExchangeRate)
                .usdAmount(usdAmount)
                .targetCurrency(quote.getTargetCurrency().name())
                .exchangeRate(quote.getExchangeRate())
                .targetAmount(quote.getTargetAmount())
                .requestedDate(formatDateTime(quote.getCreationDtm()))
                .build();
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }
}

