package com.rahim.configserver.service.implementation;

import com.rahim.configserver.service.IEncryptorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 09/03/2024
 */
@Service
@RequiredArgsConstructor
public class IEncryptorImpl implements IEncryptorService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encrypt(String plaintext) {
        return (plaintext != null && !plaintext.isEmpty()) ? passwordEncoder.encode(plaintext) : null;
    }
}
