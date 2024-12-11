package com.moin.transfer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteListApiResponse {
    private int resultCode;
    private String resultMsg;
    private String userId;
    private String name;
    private int todayTransferCount;
    private BigDecimal todayTransferUsdAmount;
    private List<TransferHistory> history;

    public static QuoteListApiResponse success(QuoteListData data) {
        return QuoteListApiResponse.builder()
                .resultCode(200)
                .resultMsg("OK")
                .userId(data.getUserId())
                .name(data.getName())
                .todayTransferCount(data.getTodayTransferCount())
                .todayTransferUsdAmount(data.getTodayTransferUsdAmount())
                .history(data.getHistory())
                .build();
    }
}

