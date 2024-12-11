package com.moin.transfer.service.transfer.fixture;

import com.moin.transfer.common.constants.FeeConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeRateFixture {
    private static final double DEFAULT_JPY_BASE_PRICE = 943.42;
    private static final double DEFAULT_USD_BASE_PRICE = FeeConstants.USD_TIER1_FIXED_FEE.doubleValue();
    private static final int JPY_CURRENCY_UNIT = 100;
    private static final int USD_CURRENCY_UNIT = 1;

    public static List<Map<String, Object>> createDefaultJpyResponse() {
        return createMockResponse(DEFAULT_JPY_BASE_PRICE, JPY_CURRENCY_UNIT);
    }

    public static List<Map<String, Object>> createDefaultUsdResponse() {
        return createMockResponse(DEFAULT_USD_BASE_PRICE, USD_CURRENCY_UNIT);
    }

    public static List<Map<String, Object>> createMockResponse(double basePrice, int currencyUnit) {
        Map<String, Object> response = new HashMap<>();
        response.put("basePrice", basePrice);
        response.put("currencyUnit", currencyUnit);
        return List.of(response);
    }
}
