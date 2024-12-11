package com.moin.transfer.service.transfer;

import com.moin.transfer.common.constants.FeeConstants;
import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class FeeCalculator {
    public BigDecimal calculateFee(long amount, Currency currency) {
        BigDecimal amountValue = BigDecimal.valueOf(amount);

        return switch (currency) {
            case USD -> calculateUsdFee(amount, amountValue);
            case JPY -> calculateJpyFee(amountValue);
            default -> throw new QuoteException(QuoteErrorCode.UNSUPPORTED_CURRENCY);
        };
    }

    private BigDecimal calculateUsdFee(long amount, BigDecimal amountValue) {
        if (amount <= FeeConstants.USD_TIER1_LIMIT) {
            return amountValue.multiply(FeeConstants.USD_TIER1_RATE)
                    .add(FeeConstants.USD_TIER1_FIXED_FEE);
        }
        return amountValue.multiply(FeeConstants.USD_TIER2_RATE)
                .add(FeeConstants.USD_TIER2_FIXED_FEE);
    }

    private BigDecimal calculateJpyFee(BigDecimal amountValue) {
        return amountValue.multiply(FeeConstants.JPY_RATE)
                .add(FeeConstants.JPY_FIXED_FEE);
    }
}


