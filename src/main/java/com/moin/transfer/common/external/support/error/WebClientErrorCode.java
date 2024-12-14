package com.moin.transfer.common.external.support.error;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WebClientErrorCode implements CommonErrorCodeType {
    API_CALL_ERROR("API_CALL_ERROR", "API 호출 중 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    INVALID_RESPONSE("INVALID_RESPONSE", "유효하지 않은 응답입니다.", HttpStatus.SERVICE_UNAVAILABLE);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}