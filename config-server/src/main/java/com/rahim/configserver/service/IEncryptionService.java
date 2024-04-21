package com.rahim.configserver.service;

import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 21/04/2024
 */
public interface IEncryptionService {

    /**
     * Decrypts the given encrypted data.
     *
     * @param encryptedData the encrypted data to decrypt
     * @return the decrypted data
     */
    String decrypt(String encryptedData);

    /**
     * Encrypts the given data.
     *
     * @param plainData the data to encrypt
     * @return the encrypted data
     */
    Map<String, String> encrypt(Map<String, String> plainTextMap);
}
