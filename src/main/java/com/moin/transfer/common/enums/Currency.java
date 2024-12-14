package com.moin.transfer.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    KRW(0),
    JPY(0),
    USD(2);

    private final int defaultFractionDigits;
}
