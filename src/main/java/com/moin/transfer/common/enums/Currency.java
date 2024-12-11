package com.moin.transfer.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    JPY(0),
    USD(2);

    private final int defaultFractionDigits;
}
