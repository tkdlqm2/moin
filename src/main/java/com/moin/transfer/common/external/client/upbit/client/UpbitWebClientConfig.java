package com.moin.transfer.common.external.client.upbit.client;

import com.moin.transfer.common.external.client.upbit.properties.UpbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class UpbitWebClientConfig {
    private final HttpClient httpClient;
    private final ExchangeStrategies exchangeStrategies;
    private final UpbitProperties properties;

    @Bean
    public WebClient upbitWebClient() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }
}