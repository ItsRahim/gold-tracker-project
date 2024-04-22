package com.rahim.configserver.config;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 21/04/2024
 */
@Component
public class AES256Config {

    @Value("${jasypt.encryptor.password}")
    private String encryptorPassword;

    @Bean
    public AES256TextEncryptor stringEncryptor() {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encryptorPassword);

        return encryptor;
    }
}
