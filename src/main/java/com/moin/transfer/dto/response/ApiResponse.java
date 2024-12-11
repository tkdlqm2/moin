package com.moin.transfer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int resultCode;
    private String resultMsg;
    private T data;

    public static ApiResponse<?> success() {
        return ApiResponse.builder()
                .resultCode(200)
                .resultMsg("OK")
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .resultCode(200)
                .resultMsg("OK")
                .data(data)
                .build();
    }

    public boolean isSuccess() {
        return this.resultCode == 200;
    }
}