package com.moin.transfer.service.user;

import com.moin.transfer.common.security.util.EncryptionUtil;
import com.moin.transfer.exception.user.UserErrorCode;
import com.moin.transfer.exception.user.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionService {
    private final EncryptionUtil encryptionUtil;
    private final PasswordEncoder passwordEncoder;

    public String encryptValue(String value) {
        return encryptionUtil.encrypt(value);
    }

    public String decryptValue(String encryptedValue) {
        try {
            return encryptionUtil.decrypt(encryptedValue);
        } catch (Exception e) {
            log.error("Failed to decrypt value: {}", e.getMessage());
            throw new UserException(UserErrorCode.DECRYPT_FAILED);
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}


