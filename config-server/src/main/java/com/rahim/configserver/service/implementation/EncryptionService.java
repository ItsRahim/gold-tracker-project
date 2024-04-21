package com.rahim.configserver.service.implementation;

import com.rahim.configserver.config.EncryptionConfig;
import com.rahim.configserver.service.IEncryptionService;
import lombok.RequiredArgsConstructor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 21/04/2024
 */
@Service
@RequiredArgsConstructor
public class EncryptionService implements IEncryptionService {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionService.class);

    private final AES256TextEncryptor encryptor;

    @Override
    public String decrypt(String encryptedData) {
        try {
            LOG.debug("Decrypting Data...");
            return encryptor.decrypt(encryptedData);
        } catch (Exception e) {
            LOG.error("Error decrypting data: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String encrypt(String plainText) {
        try {
            LOG.debug("Encrypting Plain Text...");
            return encryptor.encrypt(plainText);
        } catch (Exception e) {
            LOG.error("Error encrypting data: {}", e.getMessage());
            return null;
        }
    }
}
