package com.rahim.authenticationservice.config;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rahim Ahmed
 * @created 21/04/2024
 */
@Configuration
public class AES256Config {

    //@Value("${jasypt.encryptor.password}")
    private String encryptorPassword = "XpTTc9vf1goUo4G9h44a7ciSnln0fFdqck5vxl5iM8g=";

    @Bean
    public AES256TextEncryptor stringEncryptor() {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encryptorPassword);

        return encryptor;
    }
}
