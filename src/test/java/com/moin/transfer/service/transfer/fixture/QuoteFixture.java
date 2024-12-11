package com.moin.transfer.service.transfer.fixture;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.entity.Quote;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QuoteFixture {
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2024, 1, 1, 9, 0, 0);
    private static final String FORMATTED_DATE = "2024-01-01 09:00:00";

    public static Quote createBasicQuote(Long quoteId) {
        Quote quote = Quote.builder()
                .exchangeRate(new BigDecimal("1000.00"))
                .expireTime(TEST_DATE_TIME)
                .targetAmount(new BigDecimal("100.00"))
                .build();

        setBaseEntityFields(quote);
        ReflectionTestUtils.setField(quote, "id", quoteId);
        return quote;
    }

    public static Quote createUsdTierOneQuote(Long quoteId) {
        Quote quote = Quote.builder()
                .amount(new BigDecimal("300000.00"))
                .fee(new BigDecimal("1600.00"))     // (300,000 * 0.002) + 1000 = 1600원
                .targetCurrency(Currency.USD)
                .exchangeRate(new BigDecimal("1000.00"))
                .targetAmount(new BigDecimal("100.00"))
                .build();

        setBaseEntityFields(quote);
        ReflectionTestUtils.setField(quote, "id", quoteId);
        return quote;
    }

    public static Quote createJpyQuote(Long quoteId) {
        Quote quote = Quote.builder()
                .amount(new BigDecimal("500000.00"))
                .fee(new BigDecimal("5500.00"))     // (500,000 * 0.005) + 3000 = 5500원
                .targetCurrency(Currency.JPY)
                .exchangeRate(new BigDecimal("1000.00"))
                .targetAmount(new BigDecimal("100.00"))
                .build();

        setBaseEntityFields(quote);
        ReflectionTestUtils.setField(quote, "id", quoteId);
        return quote;
    }

    private static void setBaseEntityFields(Quote quote) {
        ReflectionTestUtils.setField(quote, "creationDtm", TEST_DATE_TIME);
        ReflectionTestUtils.setField(quote, "creationId", "SYSTEM");
        ReflectionTestUtils.setField(quote, "modifyDtm", TEST_DATE_TIME);
        ReflectionTestUtils.setField(quote, "modifyId", "SYSTEM");
    }

    public static LocalDateTime getTestDateTime() {
        return TEST_DATE_TIME;
    }

    public static String getFormattedDate() {
        return FORMATTED_DATE;
    }
}

