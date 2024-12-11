package com.moin.transfer.common.http;

import org.springframework.http.HttpStatus;

public interface HttpMessage extends Message {
    public HttpStatus getHttpStatus();
}
