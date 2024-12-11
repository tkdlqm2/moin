package com.moin.transfer.service.user;

import com.moin.transfer.entity.User;
import com.moin.transfer.exception.user.UserErrorCode;
import com.moin.transfer.exception.user.UserException;
import com.moin.transfer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.DUPLICATE_ID);
        }
    }

    public User validateAndGetUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));
    }
}

