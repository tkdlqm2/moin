package com.moin.transfer.service.transfer;

import com.moin.transfer.common.constants.FeeConstants;
import com.moin.transfer.common.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeeCalculatorTest {
    @InjectMocks
    private FeeCalculator feeCalculator;

    @BeforeEach
    void setUp() {
        feeCalculator = new FeeCalculator();
    }

    @Test
    @DisplayName("USD Tier1 수수료 계산 성공")
    void calculateUsdTier1FeeSuccess() {
        // given
        long amount = 2000;
        // (2000 * 0.002) + 1000.0 = 1004.0
        BigDecimal expectedFee = new BigDecimal("1004.000");

        // when
        BigDecimal actualFee = feeCalculator.calculateFee(amount, Currency.USD);

        // then
        assertEquals(expectedFee, actualFee);
    }

    @Test
    @DisplayName("USD Tier2 수수료 계산 성공")
    void calculateUsdTier2FeeSuccess() {
        // given
        long amount = 50000;

        // 50000 + 0.0002 + 1000
        BigDecimal expectedFee = new BigDecimal("1100.000");

        // when
        BigDecimal actualFee = feeCalculator.calculateFee(amount, Currency.USD);

        // then
        assertEquals(expectedFee, actualFee);
    }

    @Test
    @DisplayName("JPY 수수료 계산 성공")
    void calculateJpyFeeSuccess() {
        // given
        long amount = 10000;
        BigDecimal expectedFee = new BigDecimal("3050.000"); // (10000 * 0.005) + 3000.0 = 3050.0

        // when
        BigDecimal actualFee = feeCalculator.calculateFee(amount, Currency.JPY);

        // then
        assertEquals(expectedFee, actualFee);
    }

    @Test
    @DisplayName("USD Tier1 한도 경계값 테스트")
    void calculateUsdTier1BoundaryTest() {
        // given
        long amount = FeeConstants.USD_TIER1_LIMIT;
        BigDecimal expectedFee = BigDecimal.valueOf(amount)
                .multiply(FeeConstants.USD_TIER1_RATE)
                .add(FeeConstants.USD_TIER1_FIXED_FEE);

        // when
        BigDecimal actualFee = feeCalculator.calculateFee(amount, Currency.USD);

        // then
        assertEquals(expectedFee, actualFee);
    }

}