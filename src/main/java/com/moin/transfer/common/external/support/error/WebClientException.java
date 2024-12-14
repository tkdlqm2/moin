package com.moin.transfer.common.external.support.error;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class WebClientException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public WebClientException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}