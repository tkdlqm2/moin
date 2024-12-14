package com.moin.transfer.service.transfer.fixture;

import com.moin.transfer.common.enums.IdType;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class QuoteValidatorFixture {
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.now();

    public static Quote createExpiredQuote(Long quoteId) {
        Quote quote = Quote.builder()
                .expireTime(TEST_DATE_TIME.minusMinutes(1))
                .build();
        ReflectionTestUtils.setField(quote, "id", quoteId);
        return quote;
    }

    public static Quote createUsedQuote(Long quoteId) {
        Quote quote = Quote.builder()
                .expireTime(TEST_DATE_TIME.plusMinutes(30))
                .build();
        ReflectionTestUtils.setField(quote, "id", quoteId);
        ReflectionTestUtils.setField(quote, "used", true);
        return quote;
    }

    public static User createIndividualUser(Long userId) {
        return User.builder()
                .idType(IdType.REG_NO)
                .build();
    }

}
