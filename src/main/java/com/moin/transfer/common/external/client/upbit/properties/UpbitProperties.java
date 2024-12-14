package com.moin.transfer.common.external.client.upbit.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "external.api.upbit")
public class UpbitProperties {
    private String baseUrl;
    private String exchangeRatePath;
}