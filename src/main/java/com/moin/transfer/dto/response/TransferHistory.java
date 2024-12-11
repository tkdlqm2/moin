package com.moin.transfer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferHistory {
    private BigDecimal sourceAmount;
    private BigDecimal fee;
    private BigDecimal usdExchangeRate;
    private BigDecimal usdAmount;
    private String targetCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal targetAmount;
    private String requestedDate;
}
