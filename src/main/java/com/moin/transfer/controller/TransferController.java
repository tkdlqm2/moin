package com.moin.transfer.controller;

import com.moin.transfer.dto.request.QuoteRegisterRequest;
import com.moin.transfer.dto.request.QuoteRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.QuoteApiResponse;
import com.moin.transfer.dto.response.QuoteListApiResponse;
import com.moin.transfer.service.transfer.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/quote")
    @Operation(
            summary = "송금 견적서 갖고오기",
            description = "송금 견적서 가져오기."
    )
    public QuoteApiResponse createQuote(@Valid @RequestBody QuoteRequest request,
          @Parameter(hidden = true)
          @RequestHeader(value = "Authorization") String authorizationHeader
    ) {
        return transferService.createQuote(request);
    }

    @PostMapping("/request")
    @Operation(summary = "송금 견적서 등록", description = "생성된 송금 견적서를 등록합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse<?> registerQuote(
            @Valid @RequestBody QuoteRegisterRequest request,
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization") String authorizationHeader
    ) {
        return transferService.requestQuote(request);
    }

    @GetMapping("/list")
    @Operation(summary = "송금 내역 조회", description = "사용자의 송금 내역을 조회합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    public QuoteListApiResponse getQuoteList(
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization") String authorizationHeader
    ) {
        return QuoteListApiResponse.success(transferService.quoteList(authorizationHeader));

    }
}
