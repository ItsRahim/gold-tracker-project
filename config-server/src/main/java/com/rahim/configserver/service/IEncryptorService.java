package com.rahim.configserver.service;

/**
 * The {@code IEncryptorService} interface defines a contract for encrypting sensitive data.
 * Implementations of this interface provide methods to securely hash or encrypt plaintext values.
 *
 * @author Rahim Ahmed
 * @created 09/03/2024
 */
public interface IEncryptorService {

    /**
     * Encrypts the given plaintext value.
     *
     * @param plaintext The original value to be encrypted.
     * @return The encrypted representation of the plaintext.
     */
    String encrypt(String plaintext);
}
