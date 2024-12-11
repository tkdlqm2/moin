package com.moin.transfer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteApiResponse {
    private int resultCode;
    private String resultMsg;
    private QuoteData quote;

    public static QuoteApiResponse success(QuoteData quote) {
        return QuoteApiResponse.builder()
                .resultCode(200)
                .resultMsg("OK")
                .quote(quote)
                .build();
    }
}

