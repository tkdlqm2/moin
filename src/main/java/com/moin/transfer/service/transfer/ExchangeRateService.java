package com.moin.transfer.service.transfer;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.client.upbit.client.UpbitApiClient;
import com.moin.transfer.common.external.client.upbit.dto.UpbitResponse;
import com.moin.transfer.common.external.support.error.WebClientException;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {
    private final UpbitApiClient upbitApiClient;

    public BigDecimal getExchangeRate(Currency targetCurrency) {
        try {
            List<UpbitResponse> response = upbitApiClient
                    .getExchangeRate(switch (targetCurrency) {
                        case JPY -> CurrencyCode.KRW_JPY;
                        case USD -> CurrencyCode.KRW_USD;
                        default -> throw new QuoteException(QuoteErrorCode.UNSUPPORTED_CURRENCY);
                    })
                    .block();

            validateResponse(response);
            return extractExchangeRate(response.get(0), targetCurrency);
        } catch (WebClientException e) {
            log.error("Failed to get exchange rate from API: {}", e.getMessage(), e);
            throw new QuoteException(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND);
        } catch (Exception e) {
            log.error("Failed to get exchange rate: {}", e.getMessage(), e);
            throw new QuoteException(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND);
        }
    }

    private void validateResponse(List<UpbitResponse> response) {
        if (response == null || response.isEmpty() || !response.get(0).isValid()) {
            log.error("Exchange rate response is empty or invalid");
            throw new QuoteException(QuoteErrorCode.EXCHANGE_RATE_NOT_FOUND);
        }
    }

    private BigDecimal extractExchangeRate(UpbitResponse exchangeRate, Currency targetCurrency) {
        if (targetCurrency == Currency.JPY) {
            return exchangeRate.calculateAdjustedPrice();
        }
        return exchangeRate.getBasePriceAsBigDecimal();
    }
}