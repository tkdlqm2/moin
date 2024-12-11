package com.moin.transfer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteData {
    private String quoteId;
    private BigDecimal exchangeRate;
    private String expireTime;
    private BigDecimal targetAmount;
}