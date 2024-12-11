package com.moin.transfer.service.user;

import com.moin.transfer.dto.request.LoginRequest;
import com.moin.transfer.dto.request.SignUpRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.LoginApiResponse;
import com.moin.transfer.entity.User;

public interface UserService {

    ApiResponse<?> signUp(SignUpRequest request);
    LoginApiResponse login(LoginRequest request);
    User getCurrentUser();
    String decryptRegNo(String encryptedRegNo);
}
