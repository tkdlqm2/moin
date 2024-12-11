package com.moin.transfer.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    INDIVIDUAL_USER("ROLE_INDIVIDUAL", "개인회원"),
    BUSINESS_USER("ROLE_BUSINESS", "법인회원");

    private final String authority;
    private final String description;

    @Override
    public String getAuthority() {
        return authority;
    }
}