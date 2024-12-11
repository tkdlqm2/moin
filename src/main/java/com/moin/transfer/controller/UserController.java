package com.moin.transfer.controller;

import com.moin.transfer.dto.request.LoginRequest;
import com.moin.transfer.dto.request.SignUpRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.LoginApiResponse;
import com.moin.transfer.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    public ApiResponse<?> signUp(@Valid @RequestBody SignUpRequest request) {
        return userService.signUp(request);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    public LoginApiResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
