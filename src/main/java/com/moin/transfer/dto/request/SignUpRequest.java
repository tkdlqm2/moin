package com.moin.transfer.dto.request;

import com.moin.transfer.common.enums.IdType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @Schema(description = "이메일", example = "test@gmail.com")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String userId;

    @Schema(description = "비밀번호", example = "Test123!@#")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @Schema(description = "이름", example = "홍길동")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @Schema(description = "회원 유형(REG_NO: 개인회원, BUSINESS_NO: 법인회원)", example = "REG_NO")
    @NotNull(message = "회원 유형은 필수 입력값입니다.")
    private IdType idType;

    @Schema(description = "식별값(주민번호 또는 사업자번호)", example = "860824-1655068")
    @NotBlank(message = "식별값은 필수 입력값입니다.")
    private String idValue;

    @Builder
    public SignUpRequest(String userId, String password, String name, IdType idType, String idValue) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.idType = idType;
        this.idValue = idValue;
    }
}

