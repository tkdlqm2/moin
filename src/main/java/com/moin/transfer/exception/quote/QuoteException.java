package com.moin.transfer.exception.quote;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.Getter;

@Getter
public class QuoteException extends RuntimeException {
    private final CommonErrorCodeType commonErrorCodeType;

    public QuoteException(CommonErrorCodeType commonErrorCodeType) {
        super(commonErrorCodeType.getMessage());
        this.commonErrorCodeType = commonErrorCodeType;
    }
}
