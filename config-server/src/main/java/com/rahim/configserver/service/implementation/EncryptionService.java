package com.rahim.configserver.service.implementation;

import com.rahim.configserver.service.IEncryptionService;
import lombok.RequiredArgsConstructor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> encrypt(Map<String, String> plainTextMap) {
        Map<String, String> encryptedDataMap = new HashMap<>();
        try {
            LOG.debug("Encrypting Plain Text...");

            for (Map.Entry<String, String> entry : plainTextMap.entrySet()) {
                String plainText = entry.getValue();
                if (plainText != null && !plainText.isEmpty()) {
                    String encryptedData = encryptor.encrypt(plainText);
                    encryptedDataMap.put(entry.getKey(), encryptedData);
                } else {
                    LOG.warn("Skipping encryption for empty or null value with key: {}", entry.getKey());
                }
            }

            LOG.debug("Encryption completed successfully");
            return encryptedDataMap;
        } catch (Exception e) {
            LOG.error("Error encrypting data: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}
