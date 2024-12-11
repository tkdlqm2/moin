package com.moin.transfer.common.external.client;


import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.config.ExternalApiProperties;
import com.moin.transfer.exception.external.ExternalApiErrorCode;
import com.moin.transfer.exception.external.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientApiClient implements ExternalApiClient {
    private final WebClient webClient;
    private final ExternalApiProperties properties;

    @Override
    public <O> Mono<O> callApi(CurrencyCode... currencyCodes) {
        String baseUrl = properties.getHost() + properties.getPath();
        String fullUrl = baseUrl + buildQueryParameter(currencyCodes);
        log.info("Calling external API: {}", fullUrl);

        return webClient
                .get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<O>() {})
                .timeout(Duration.ofSeconds(25))
                .onErrorResume(TimeoutException.class, e -> {
                    log.error("External API timeout: {}", e.getMessage());
                    throw new ExternalApiException(ExternalApiErrorCode.API_TIMEOUT);
                })
                .onErrorResume(WebClientRequestException.class, e -> {
                    log.error("External API connection error: {}", e.getMessage());
                    throw new ExternalApiException(ExternalApiErrorCode.API_CONNECTION_ERROR);
                });
    }

    private String buildQueryParameter(CurrencyCode... currencyCodes) {
        return "?codes=" + "," + Arrays.stream(currencyCodes)
                .map(CurrencyCode::getCode)
                .collect(Collectors.joining(","));
    }

}


