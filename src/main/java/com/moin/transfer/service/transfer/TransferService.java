package com.moin.transfer.service.transfer;

import com.moin.transfer.dto.request.QuoteRegisterRequest;
import com.moin.transfer.dto.request.QuoteRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.QuoteApiResponse;
import com.moin.transfer.dto.response.QuoteListData;

public interface TransferService {

    QuoteApiResponse createQuote(QuoteRequest request);
    ApiResponse<?> requestQuote(QuoteRegisterRequest quoteRegisterRequest);
    QuoteListData quoteList(String authorizationHeader);
}
