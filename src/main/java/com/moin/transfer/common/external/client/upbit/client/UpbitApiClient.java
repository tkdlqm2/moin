package com.moin.transfer.common.external.client.upbit.client;

import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.client.upbit.dto.UpbitRequest;
import com.moin.transfer.common.external.client.upbit.dto.UpbitResponse;
import com.moin.transfer.common.external.client.upbit.properties.UpbitProperties;
import com.moin.transfer.common.external.support.WebClientSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpbitApiClient {
    private final WebClient upbitWebClient;
    private final WebClientSupport webClientSupport;
    private final UpbitProperties properties;

    public Mono<List<UpbitResponse>> getExchangeRate(CurrencyCode currencyCode) {
        UpbitRequest request = UpbitRequest.of(currencyCode);

        return webClientSupport.exchangeToMono(
                upbitWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path(properties.getExchangeRatePath())
                                .queryParams(request.toQueryParams())
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UpbitResponse>>() {})
                        .doOnNext(response -> {
                            log.info("Raw API Response: {}", response);
                            if (!response.isEmpty()) {
                                log.info("First item in response: {}", response.get(0));
                            }
                        })
        );
    }
}