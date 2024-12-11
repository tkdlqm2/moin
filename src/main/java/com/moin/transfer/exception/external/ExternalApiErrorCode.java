package com.moin.transfer.exception.external;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExternalApiErrorCode implements CommonErrorCodeType {
    API_TIMEOUT("API_TIMEOUT", "API 요청 시간이 초과되었습니다.", HttpStatus.GATEWAY_TIMEOUT),
    API_SERVER_ERROR("API_SERVER_ERROR", "외부 API 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    API_CONNECTION_ERROR("API_CONNECTION_ERROR", "API 서버 연결에 실패했습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    INVALID_RESPONSE("INVALID_RESPONSE", "올바르지 않은 응답 형식입니다.", HttpStatus.BAD_GATEWAY),
    DATA_PARSING_ERROR("DATA_PARSING_ERROR", "데이터 파싱 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("INVALID_REQUEST", "올바르지 않은 요청 형식입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}