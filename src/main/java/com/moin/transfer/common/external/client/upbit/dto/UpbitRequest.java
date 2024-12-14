package com.moin.transfer.common.external.client.upbit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moin.transfer.common.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpbitRequest {
    private String code;
    private Integer count;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public static UpbitRequest of(CurrencyCode currencyCode) {
        return UpbitRequest.builder()
                .code(currencyCode.getCode())
                .count(1)
                .build();
    }

    public MultiValueMap<String, String> toQueryParams() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("codes", this.code);

        if (this.count != null) {
            queryParams.add("count", String.valueOf(this.count));
        }

        return queryParams;
    }
}