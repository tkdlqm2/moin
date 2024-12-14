package com.moin.transfer.service.transfer.fixture;

import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.client.upbit.dto.UpbitResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class UpbitResponseFixture {
    private static final Map<CurrencyCode, String> CURRENCY_NAMES = Map.of(
            CurrencyCode.KRW_USD, "달러",
            CurrencyCode.KRW_JPY, "엔화"
    );

    public static UpbitResponse create(CurrencyCode currencyCode, BigDecimal exchangeRate) {
        UpbitResponse response = new UpbitResponse();
        response.setCode(currencyCode.getCode());
        response.setBasePrice(exchangeRate.doubleValue());
        response.setCurrencyCode(currencyCode.name());
        response.setCurrencyName(CURRENCY_NAMES.get(currencyCode));
        return response;
    }

    public static List<UpbitResponse> createDefaultUsdResponse() {
        return List.of(create(
                CurrencyCode.KRW_USD,
                QuoteAmountFixture.DEFAULT_USD_EXCHANGE_RATE
        ));
    }

    public static List<UpbitResponse> createDefaultJpyResponse() {
        return List.of(create(
                CurrencyCode.KRW_JPY,
                new BigDecimal("9.43")
        ));
    }
}