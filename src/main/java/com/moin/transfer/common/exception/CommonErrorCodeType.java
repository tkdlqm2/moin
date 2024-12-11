package com.moin.transfer.common.exception;

import org.springframework.http.HttpStatus;

public interface CommonErrorCodeType {
    String getErrorCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
