package com.moin.transfer.exception.quote;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum QuoteErrorCode implements CommonErrorCodeType {
    NEGATIVE_NUMBER("NEGATIVE_NUMBER", "금액은 양수여야 합니다.", HttpStatus.BAD_REQUEST),
    QUOTE_EXPIRED("QUOTE_EXPIRED", "견적서가 만료되었습니다.", HttpStatus.BAD_REQUEST),
    QUOTE_ALREADY_USED("QUOTE_ALREADY_USED", "이미 사용된 견적서입니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_CURRENCY("UNSUPPORTED_CURRENCY", "지원하지 않는 통화입니다.", HttpStatus.BAD_REQUEST),
    EXCHANGE_RATE_NOT_FOUND("EXCHANGE_RATE_NOT_FOUND", "환율 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    QUOTE_NOT_FOUND("QUOTE_NOT_FOUND", "견적서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    LIMIT_EXCESS("LIMIT_EXCESS", "일일 송금 한도를 초과하였습니다.", HttpStatus.BAD_REQUEST);


    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}