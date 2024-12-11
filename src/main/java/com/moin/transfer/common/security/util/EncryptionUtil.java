package com.moin.transfer.common.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class EncryptionUtil {
    @Value("${encryption.key}")
    private String key;

    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    public String encrypt(String value) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            log.error("Encryption failed: {}", e.getMessage());
            throw new RuntimeException("Failed to encrypt value", e);
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedBytes, CHARSET);

        } catch (Exception e) {
            log.error("Decryption failed: {}", e.getMessage());
            throw new RuntimeException("Failed to decrypt value", e);
        }
    }
}
