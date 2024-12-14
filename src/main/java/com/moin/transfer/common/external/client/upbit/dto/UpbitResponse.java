package com.moin.transfer.common.external.client.upbit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpbitResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("currencyName")
    private String currencyName;

    @JsonProperty("country")
    private String country;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("date")
    private String date;

    @JsonProperty("time")
    private String time;

    @JsonProperty("basePrice")
    private Double basePrice;

    @JsonProperty("openingPrice")
    private Double openingPrice;

    @JsonProperty("highPrice")
    private Double highPrice;

    @JsonProperty("lowPrice")
    private Double lowPrice;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("currencyUnit")
    private Integer currencyUnit;

    @JsonIgnore
    public boolean isValid() {
        return code != null && !code.isEmpty() && country != null && !country.isEmpty();
    }

    @JsonIgnore
    public BigDecimal getBasePriceAsBigDecimal() {
        return basePrice != null ? BigDecimal.valueOf(basePrice) : BigDecimal.ZERO;
    }

    @JsonIgnore
    public BigDecimal calculateAdjustedPrice() {
        if (currencyUnit != null && currencyUnit > 1) {
            return getBasePriceAsBigDecimal()
                    .divide(BigDecimal.valueOf(currencyUnit), 2, RoundingMode.HALF_UP);
        }
        return getBasePriceAsBigDecimal();
    }
}