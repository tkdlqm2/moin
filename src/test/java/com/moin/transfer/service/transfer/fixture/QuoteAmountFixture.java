package com.moin.transfer.service.transfer.fixture;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.dto.response.QuoteAmount;

import java.math.BigDecimal;
import java.util.List;

public class QuoteAmountFixture {
    public static final BigDecimal DEFAULT_USD_EXCHANGE_RATE = new BigDecimal("1300");
    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal("10000000");
    private static final BigDecimal DEFAULT_TARGET_AMOUNT = new BigDecimal("5000");

    public record QuoteAmountParams(
            BigDecimal amount,
            Currency currency,
            BigDecimal targetAmount
    ) {}

    public static QuoteAmountParams createUsdQuoteAmountParams() {
        return new QuoteAmountParams(
                DEFAULT_AMOUNT,
                Currency.USD,
                DEFAULT_TARGET_AMOUNT
        );
    }

    public static List<QuoteAmount> createExceededDailyQuotes() {
        return List.of(new QuoteAmount(
                DEFAULT_AMOUNT,
                Currency.USD,
                DEFAULT_TARGET_AMOUNT
        ));
    }
}
