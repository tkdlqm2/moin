package com.moin.transfer.common.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
public class JwtProperties {
    private String secret;
    private Long accessTokenValidity;
}