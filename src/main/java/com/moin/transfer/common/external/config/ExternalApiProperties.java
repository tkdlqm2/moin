package com.moin.transfer.common.external.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.upbit")
public class ExternalApiProperties {
    @NotBlank
    private String host;
    @NotBlank
    private String path;
    private int connectTimeout = 5000;
    private int readTimeout = 5000;
}
