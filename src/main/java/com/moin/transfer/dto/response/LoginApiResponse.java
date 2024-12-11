package com.moin.transfer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginApiResponse {
    private int resultCode;
    private String resultMsg;
    private String token;

    public static LoginApiResponse success(String token) {
        return LoginApiResponse.builder()
                .resultCode(200)
                .resultMsg("OK")
                .token(token)
                .build();
    }
}