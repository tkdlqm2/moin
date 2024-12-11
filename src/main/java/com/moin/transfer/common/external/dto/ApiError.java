package com.moin.transfer.common.external.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> detail;

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // 상세 정보 추가 메서드
    public void addDetail(String key, String value) {
        if (detail == null) {
            detail = new HashMap<>();
        }
        detail.put(key, value);
    }
}
