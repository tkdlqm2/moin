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
public class QuoteListData {
    private String userId;
    private String name;
    private int todayTransferCount;
    private BigDecimal todayTransferUsdAmount;
    private List<TransferHistory> history;
}