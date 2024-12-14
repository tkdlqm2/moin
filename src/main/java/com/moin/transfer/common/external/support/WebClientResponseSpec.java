package com.moin.transfer.common.external.support;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class WebClientResponseSpec<T> {
    private final HttpStatus status;
    private final T data;
}