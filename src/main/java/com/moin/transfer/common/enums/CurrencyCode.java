package com.moin.transfer.common.enums;

public enum CurrencyCode {
    KRW_JPY("FRX.KRWJPY"),
    KRW_USD("FRX.KRWUSD");

    private final String code;

    CurrencyCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
