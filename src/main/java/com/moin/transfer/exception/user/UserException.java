package com.moin.transfer.exception.user;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public UserException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}
