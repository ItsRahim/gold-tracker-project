package com.rahim.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Rahim Ahmed
 * @created 22/06/2024
 */
@Configuration
public class BCryptConfig {

    private static final int PASSWORD_STRENGTH = 10;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_STRENGTH);
    }
}
