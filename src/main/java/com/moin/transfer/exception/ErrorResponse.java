package com.moin.transfer.exception;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    private List<String> errors;

    public static ErrorResponse create(CommonErrorCodeType errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .build();
    }
}
