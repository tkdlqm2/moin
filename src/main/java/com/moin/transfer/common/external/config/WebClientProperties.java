package com.moin.transfer.common.external.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {
    private Integer connectTimeout = 3000;
    private Integer readTimeout = 5000;
    private Integer maxInMemorySize = 16 * 1024 * 1024;
}