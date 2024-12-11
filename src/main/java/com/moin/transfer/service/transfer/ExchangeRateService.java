package com.moin.transfer.service.transfer;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.client.ExternalApiClient;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {
    private final ExternalApiClient externalApiClient;

    public BigDecimal getExchangeRate(Currency targetCurrency) {
        try {
            List<Map<String, Object>> response = externalApiClient
                    .<List<Map<String, Object>>>callApi(
                            switch (targetCurrency) {
                                case JPY -> CurrencyCode.KRW_JPY;
                                case USD -> CurrencyCode.KRW_USD;
                                default -> throw new QuoteException(QuoteErrorCode.UNSUPPORTED_CURRENCY);
                            }
                    ).block();

            validateResponse(response);
            return extractExchangeRate(response.get(0), targetCurrency);
        } catch (Exception e) {
            log.error("Failed to get exchange rate: {}", e.getMessage(), e);
            throw new QuoteException(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND);
        }
    }

    public BigDecimal getUsdExchangeRate() {
        try {
            List<Map<String, Object>> response = externalApiClient
                    .<List<Map<String, Object>>>callApi(CurrencyCode.KRW_USD)
                    .block();

            validateResponse(response);
            return new BigDecimal(response.get(0).get("basePrice").toString());
        } catch (Exception e) {
            log.error("Failed to get USD exchange rate: {}", e.getMessage(), e);
            throw new QuoteException(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND);
        }
    }

    private void validateResponse(List<Map<String, Object>> response) {
        if (response == null || response.isEmpty()) {
            log.error("Exchange rate response is empty");
            throw new QuoteException(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND);
        }
    }

    private BigDecimal extractExchangeRate(Map<String, Object> exchangeRate, Currency targetCurrency) {
        BigDecimal basePrice = new BigDecimal(exchangeRate.get("basePrice").toString());
        if (targetCurrency == Currency.JPY) {
            int currencyUnit = Integer.parseInt(exchangeRate.get("currencyUnit").toString());
            return basePrice.divide(BigDecimal.valueOf(currencyUnit), 2, RoundingMode.HALF_UP);
        }
        return basePrice;
    }
}

