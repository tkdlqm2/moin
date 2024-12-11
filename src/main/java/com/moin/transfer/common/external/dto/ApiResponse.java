package com.moin.transfer.common.external.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;
    private ApiError error;
    private String status;

    @Builder
    public ApiResponse(T data, ApiError error, String status) {
        this.data = data;
        this.error = error;
        this.status = status;
    }

    // 성공 응답 생성
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .status("success")
                .build();
    }

    // 실패 응답 생성
    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .error(new ApiError(code, message))
                .status("fail")
                .build();
    }
}
