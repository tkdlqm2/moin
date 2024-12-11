package com.moin.transfer.service.user;

import com.moin.transfer.common.security.jwt.JwtTokenProvider;
import com.moin.transfer.exception.user.UserErrorCode;
import com.moin.transfer.exception.user.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String BEARER_PREFIX = "Bearer ";

    public Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public String generateToken(Authentication authentication) {
        return jwtTokenProvider.generateAccessToken(authentication);
    }

    public String validateTokenAndGetUserId(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new UserException(UserErrorCode.BAD_TOKEN);
        }
        String token = authorizationHeader.substring(7);
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}

