package com.moin.transfer.service.transfer;

import com.moin.transfer.common.constants.FeeConstants;
import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class FeeCalculator {
    public BigDecimal calculateFee(long amount, Currency currency) {
        BigDecimal amountValue = BigDecimal.valueOf(amount);
        int currencyScale = Math.min(12, Currency.valueOf(currency.name()).getDefaultFractionDigits());

        return switch (currency) {
            case USD -> calculateUsdFee(amount, amountValue, currencyScale);
            case JPY -> calculateJpyFee(amountValue, currencyScale);
            default -> throw new QuoteException(QuoteErrorCode.UNSUPPORTED_CURRENCY);
        };
    }

    private BigDecimal calculateUsdFee(long amount, BigDecimal amountValue, int scale) {
        if (amount <= FeeConstants.USD_TIER1_LIMIT) {
            return amountValue.multiply(FeeConstants.USD_TIER1_RATE)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .add(FeeConstants.USD_TIER1_FIXED_FEE)
                    .setScale(scale, RoundingMode.HALF_UP);
        }
        return amountValue.multiply(FeeConstants.USD_TIER2_RATE)
                .setScale(scale, RoundingMode.HALF_UP)
                .add(FeeConstants.USD_TIER2_FIXED_FEE)
                .setScale(scale, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateJpyFee(BigDecimal amountValue, int scale) {
        return amountValue.multiply(FeeConstants.JPY_RATE)
                .setScale(scale, RoundingMode.HALF_UP)
                .add(FeeConstants.JPY_FIXED_FEE)
                .setScale(scale, RoundingMode.HALF_UP);
    }
}
