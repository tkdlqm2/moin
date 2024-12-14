package com.moin.transfer.dto.response;

import com.moin.transfer.common.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class QuoteAmount {
    private BigDecimal targetAmount;
    private Currency targetCurrency;
    private BigDecimal amount;
}
