package com.rahim.userservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Rahim Ahmed
 * @created 22/06/2024
 */
@Component
@RequiredArgsConstructor
public class PasswordUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    public char[] encryptPassword(String password) {
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword.toCharArray();
    }

    public boolean checkPassword(String clearPassword, char[] hashedPassword) {
        String hashedPasswordString = new String(hashedPassword);
        boolean passwordMatches = passwordEncoder.matches(clearPassword, hashedPasswordString);

        if (passwordMatches) {
            clearCharArray(hashedPassword);
        }

        return passwordMatches;
    }

    private void clearCharArray(char[] hashedPassword) {
        Arrays.fill(hashedPassword, '\u0000');
    }

}
