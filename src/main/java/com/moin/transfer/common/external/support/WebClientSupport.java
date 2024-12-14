package com.moin.transfer.common.external.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientSupport {
    public <T> Mono<T> exchangeToMono(Mono<T> requestSpec) {
        return requestSpec
                .doOnError(this::logError);
    }

    private void logError(Throwable error) {
        log.error("WebClient error: {}", error.getMessage(), error);
    }
}