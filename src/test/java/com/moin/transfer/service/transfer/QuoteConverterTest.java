package com.moin.transfer.service.transfer;

import com.moin.transfer.dto.response.QuoteData;
import com.moin.transfer.dto.response.TransferHistory;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.service.transfer.fixture.QuoteFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuoteConverterTest {

    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2024, 1, 1, 9, 0, 0);
    private static final String FORMATTED_DATE = "2024-01-01 09:00:00";

    @Test
    @DisplayName("Quote를 QuoteData로 변환 성공")
    void toQuoteDataSuccess() {
        // given
        Long quoteId = 1L;
        Quote quote = QuoteFixture.createBasicQuote(quoteId);

        // when
        QuoteData quoteData = QuoteConverter.toQuoteData(quote);

        // then
        assertEquals(quoteId.toString(), quoteData.getQuoteId());
        assertEquals(new BigDecimal("1000.00"), quoteData.getExchangeRate());
        assertEquals(QuoteFixture.getFormattedDate(), quoteData.getExpireTime());
        assertEquals(new BigDecimal("100.00"), quoteData.getTargetAmount());
    }

    @Test
    @DisplayName("Quote 리스트를 TransferHistory 리스트로 변환 성공")
    void toTransferHistoryListSuccess() {
        // given
        BigDecimal usdExchangeRate = new BigDecimal("1300.00");
        Quote usdQuote = QuoteFixture.createUsdTierOneQuote(1L);
        Quote jpyQuote = QuoteFixture.createJpyQuote(2L);
        List<Quote> quotes = List.of(usdQuote, jpyQuote);

        // when
        List<TransferHistory> histories = QuoteConverter.toTransferHistoryList(quotes, usdExchangeRate);

        // then
        assertEquals(2, histories.size());

        // USD 송금 이력 검증
        TransferHistory usdHistory = histories.get(0);
        assertEquals(new BigDecimal("300000.00"), usdHistory.getSourceAmount());
        assertEquals(new BigDecimal("1600.00"), usdHistory.getFee());
        assertEquals(new BigDecimal("1300.00"), usdHistory.getUsdExchangeRate());
        assertEquals(new BigDecimal("229.54"), usdHistory.getUsdAmount());
        assertEquals("USD", usdHistory.getTargetCurrency());
        assertEquals(QuoteFixture.getFormattedDate(), usdHistory.getRequestedDate());
    }



    @Test
    @DisplayName("빈 Quote 리스트를 변환하면 빈 TransferHistory 리스트 반환")
    void toTransferHistoryListWithEmptyQuotes() {
        // given
        List<Quote> emptyQuotes = Collections.emptyList();
        BigDecimal usdExchangeRate = new BigDecimal("1300.00");

        // when
        List<TransferHistory> histories = QuoteConverter.toTransferHistoryList(emptyQuotes, usdExchangeRate);

        // then
        assertTrue(histories.isEmpty());
    }
}