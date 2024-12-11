package com.moin.transfer.common.external.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.upbit")
public class ExternalApiProperties {
    private String host;
    private String path;
}
