package com.moin.transfer.exception.external;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public ExternalApiException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}