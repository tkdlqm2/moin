package com.moin.transfer.service.user.fixture;

import com.moin.transfer.common.enums.IdType;
import com.moin.transfer.common.enums.Role;
import com.moin.transfer.dto.request.LoginRequest;
import com.moin.transfer.dto.request.SignUpRequest;
import com.moin.transfer.entity.User;

public class UserFixture {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "password123";
    public static final String NAME = "Test User";
    public static final String REG_NO = "123456-1234567";
    public static final String ENCODED_PASSWORD = "encodedPassword";
    public static final String ENCRYPTED_ID_VALUE = "encryptedIdValue";
    public static final String AUTH_TOKEN = "generatedToken";

    public static SignUpRequest createSignUpRequest() {
        return SignUpRequest.builder()
                .userId(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .idType(IdType.REG_NO)
                .idValue(REG_NO)
                .build();
    }

    public static LoginRequest createLoginRequest() {
        return new LoginRequest(EMAIL, PASSWORD);
    }

    public static User createUser() {
        return User.builder()
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .name(NAME)
                .idType(IdType.REG_NO)
                .idValue(ENCRYPTED_ID_VALUE)
                .role(Role.INDIVIDUAL_USER)
                .build();
    }
}
