package com.moin.transfer.exception.user;

import com.moin.transfer.common.exception.CommonErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements CommonErrorCodeType {
    NOT_FOUND_USER("NOT_FOUND_USER", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DECRYPT_FAILED("DECRYPT_FAILED", "주민번호 복호화를 실패했습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_ID("DUPLICATE_ID", "이미 사용중인 아이디입니다.", HttpStatus.BAD_REQUEST),
    BAD_TOKEN("BAD_TOKEN", "잘못된 토큰 값입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_ENCRYPTION_ERROR("PASSWORD_ENCRYPTION_ERROR", "비밀번호 암호화 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_LOGIN_ATTEMPT("INVALID_LOGIN_ATTEMPT", "로그인 시도 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),

    INVALID_REQUEST("INVALID_REQUEST", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT("INVALID_EMAIL_FORMAT", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_FORMAT("INVALID_PASSWORD_FORMAT", "비밀번호 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    REQUIRED_FIELD_MISSING("REQUIRED_FIELD_MISSING", "필수 필드가 누락되었습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}