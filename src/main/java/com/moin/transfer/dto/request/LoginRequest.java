package com.moin.transfer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    @Schema(description = "이메일", example = "test@gmail.com")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String userId;

    @Schema(description = "비밀번호", example = "Test123!@#")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @Builder
    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}


