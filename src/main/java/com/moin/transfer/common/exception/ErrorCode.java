package com.moin.transfer.common.exception;

import com.moin.transfer.common.http.HttpMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorCode implements HttpMessage {
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown server error"),
            ;

    private final HttpStatus httpStatus;
    private final String message;

    public static ErrorCode of(HttpStatus httpStatus) {
        if (httpStatus.is4xxClientError()) {
            return ErrorCode.BAD_REQUEST_ERROR;
        } else {
            return ErrorCode.UNKNOWN_SERVER_ERROR;
        }
    }
}
