package com.moin.transfer.dto.request;

import com.moin.transfer.common.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuoteRequest {

    @Schema(description = "amount", example = "6900000")
    @Positive(message = "송금액은 양수여야 합니다")
    private Long amount;

    @Schema(description = "targetCurrency", example = "JPY")
    @NotNull(message = "수취 통화는 필수입니다")
    private Currency targetCurrency;

    @Builder
    public QuoteRequest(Long amount, Currency targetCurrency) {
        this.amount = amount;
        this.targetCurrency = targetCurrency;
    }
}
