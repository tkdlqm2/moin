package com.moin.transfer.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuoteRegisterRequest {
    private long quoteId;

    @Builder
    public QuoteRegisterRequest(long quoteId) {
        this.quoteId = quoteId;
    }
}
