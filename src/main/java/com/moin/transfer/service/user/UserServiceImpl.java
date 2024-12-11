package com.moin.transfer.service.user;

import com.moin.transfer.common.enums.IdType;
import com.moin.transfer.common.enums.Role;
import com.moin.transfer.dto.request.LoginRequest;
import com.moin.transfer.dto.request.SignUpRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.LoginApiResponse;
import com.moin.transfer.entity.User;
import com.moin.transfer.exception.user.UserErrorCode;
import com.moin.transfer.exception.user.UserException;
import com.moin.transfer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final AuthenticationService authenticationService;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public ApiResponse<?> signUp(SignUpRequest request) {
        userValidator.validateDuplicateEmail(request.getUserId());

        User user = createUser(request);
        userRepository.save(user);

        return ApiResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public LoginApiResponse login(LoginRequest request) {
        User user = userValidator.validateAndGetUser(request.getUserId());
        validatePassword(request.getPassword(), user.getPassword());

        Authentication authentication = authenticationService.authenticateUser(
                request.getUserId(),
                request.getPassword()
        );

        String token = authenticationService.generateToken(authentication);
        return LoginApiResponse.success(token);
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserException(UserErrorCode.BAD_TOKEN);
        }

        String username = authentication.getName();
        return userValidator.validateAndGetUser(username);
    }

    @Override
    public String decryptRegNo(String encryptedRegNo) {
        return encryptionService.decryptValue(encryptedRegNo);
    }

    private User createUser(SignUpRequest request) {
        return User.builder()
                .email(request.getUserId())
                .password(encryptionService.encodePassword(request.getPassword()))
                .name(request.getName())
                .idType(request.getIdType())
                .idValue(encryptionService.encryptValue(request.getIdValue()))
                .role(determineUserRole(request.getIdType()))
                .build();
    }

    private Role determineUserRole(IdType idType) {
        return idType == IdType.REG_NO ? Role.INDIVIDUAL_USER : Role.BUSINESS_USER;
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!encryptionService.matchesPassword(rawPassword, encodedPassword)) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
    }
}


