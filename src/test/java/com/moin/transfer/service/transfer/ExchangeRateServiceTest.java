package com.moin.transfer.service.transfer;

import com.moin.transfer.common.constants.FeeConstants;
import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.client.ExternalApiClient;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import com.moin.transfer.service.transfer.fixture.ExchangeRateFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExternalApiClient externalApiClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    @DisplayName("JPY 환율 조회 성공")
    void getJpyExchangeRateSuccess() {
        // Given
        BigDecimal expectedJpyRate = new BigDecimal("9.43");
        when(externalApiClient.<List<Map<String, Object>>>callApi(CurrencyCode.KRW_JPY))
                .thenReturn(Mono.just(ExchangeRateFixture.createDefaultJpyResponse()));

        // When
        BigDecimal actualRate = exchangeRateService.getExchangeRate(Currency.JPY);

        // Then
        assertEquals(expectedJpyRate, actualRate);
        verify(externalApiClient).callApi(CurrencyCode.KRW_JPY);
    }

    @Test
    @DisplayName("USD 환율 조회 성공")
    void getUsdExchangeRateSuccess() {
        // given
        BigDecimal expectedUsdRate = FeeConstants.USD_TIER1_FIXED_FEE;
        when(externalApiClient.<List<Map<String, Object>>>callApi(CurrencyCode.KRW_USD))
                .thenReturn(Mono.just(ExchangeRateFixture.createDefaultUsdResponse()));

        // when
        BigDecimal actualRate = exchangeRateService.getExchangeRate(Currency.USD);

        // then
        assertEquals(expectedUsdRate, actualRate);
        verify(externalApiClient).callApi(CurrencyCode.KRW_USD);
    }

    @Test
    @DisplayName("환율 API 응답이 비어있을 경우 예외 발생")
    void getExchangeRateWithEmptyResponse() {
        // given
        when(externalApiClient.callApi(any()))
                .thenReturn(Mono.just(Collections.emptyList()));

        // when
        QuoteException exception = assertThrows(QuoteException.class, () -> exchangeRateService.getExchangeRate(Currency.USD));

        // then
        assertEquals(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND, exception.getCommonErrorCodeType());
    }
}