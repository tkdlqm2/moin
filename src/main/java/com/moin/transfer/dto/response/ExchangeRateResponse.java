package com.moin.transfer.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExchangeRateResponse {
    private Long id;
    private String code;
    private String currencyCode;
    private BigDecimal basePrice;
    private Integer currencyUnit;
}
