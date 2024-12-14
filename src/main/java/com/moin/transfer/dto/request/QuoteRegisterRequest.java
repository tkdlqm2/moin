package com.moin.transfer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuoteRegisterRequest {

    @Schema(description = "targetCurrency", example = "1")
    @NotNull(message = "아이디값은 필수 입니다.")
    private long quoteId;

    @Builder
    public QuoteRegisterRequest(long quoteId) {
        this.quoteId = quoteId;
    }
}
