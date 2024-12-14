package com.moin.transfer.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeeConstants {
    public static final BigDecimal USD_TIER1_RATE = new BigDecimal("0.002");
    public static final BigDecimal USD_TIER2_RATE = new BigDecimal("0.001");
    public static final BigDecimal JPY_RATE = new BigDecimal("0.005");

    public static final BigDecimal USD_TIER1_FIXED_FEE = new BigDecimal("1000.0");
    public static final BigDecimal USD_TIER2_FIXED_FEE = new BigDecimal("3000");
    public static final BigDecimal JPY_FIXED_FEE = new BigDecimal("3000");

    public static final long USD_TIER1_LIMIT = 1400000; // $1 = 1400 원 기준

    // 개인회원 일일 송금 한도
    public static final BigDecimal INDIVIDUAL_DAILY_LIMIT = new BigDecimal("1000"); // 달러 기준

    // 법인회원 일일 송금 한도
    public static final BigDecimal BUSINESS_DAILY_LIMIT = new BigDecimal("5000"); // 달러 기준
}



