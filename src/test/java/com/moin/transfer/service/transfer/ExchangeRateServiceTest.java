package com.moin.transfer.service.transfer;

import com.moin.transfer.common.constants.FeeConstants;
import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.client.upbit.client.UpbitApiClient;
import com.moin.transfer.common.external.client.upbit.dto.UpbitResponse;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private UpbitApiClient upbitApiClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    @DisplayName("JPY 환율 조회 성공")
    void getJpyExchangeRateSuccess() {
        // Given
        BigDecimal expectedJpyRate = new BigDecimal("9.43");
        List<UpbitResponse> mockResponse = List.of(createMockJpyResponse());
        when(upbitApiClient.getExchangeRate(CurrencyCode.KRW_JPY))
                .thenReturn(Mono.just(mockResponse));

        // When
        BigDecimal actualRate = exchangeRateService.getExchangeRate(Currency.JPY);

        // Then
        assertEquals(expectedJpyRate, actualRate);
        verify(upbitApiClient).getExchangeRate(CurrencyCode.KRW_JPY);
    }

    @Test
    @DisplayName("USD 환율 조회 성공")
    void getUsdExchangeRateSuccess() {
        // Given
        BigDecimal expectedUsdRate = FeeConstants.USD_TIER1_FIXED_FEE;
        List<UpbitResponse> mockResponse = List.of(createMockUsdResponse());
        when(upbitApiClient.getExchangeRate(CurrencyCode.KRW_USD))
                .thenReturn(Mono.just(mockResponse));

        // When
        BigDecimal actualRate = exchangeRateService.getExchangeRate(Currency.USD);

        // Then
        assertEquals(expectedUsdRate, actualRate);
        verify(upbitApiClient).getExchangeRate(CurrencyCode.KRW_USD);
    }

    @Test
    @DisplayName("환율 API 응답이 비어있을 경우 예외 발생")
    void getExchangeRateWithEmptyResponse() {
        // Given
        when(upbitApiClient.getExchangeRate(any()))
                .thenReturn(Mono.just(Collections.emptyList()));

        // When & Then
        QuoteException exception = assertThrows(QuoteException.class,
                () -> exchangeRateService.getExchangeRate(Currency.USD));
        assertEquals(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND, exception.getCommonErrorCodeType());
    }

    private UpbitResponse createMockJpyResponse() {
        UpbitResponse response = new UpbitResponse();
        response.setCode("FRX.KRWJPY");
        response.setBasePrice(943.0);  // 100엔 당 943원
        response.setCurrencyUnit(100);  // 100엔 단위
        response.setCountry("일본");
        return response;
    }

    private UpbitResponse createMockUsdResponse() {
        UpbitResponse response = new UpbitResponse();
        response.setCode("FRX.KRWUSD");
        response.setBasePrice(FeeConstants.USD_TIER1_FIXED_FEE.doubleValue());
        response.setCurrencyUnit(1);
        response.setCountry("미국");
        return response;
    }
}