package com.rahim.emailservice.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class is a utility that validates email templates.
 * It checks if the email body, placeholders, and tokens are not null,
 * and if the number of distinct tokens matches the number of placeholders.
 *
 * @author Rahim Ahmed
 * @since 12/12/2023
 */
@Component
@RequiredArgsConstructor
public class EmailValidator {

    private static final Logger log = LoggerFactory.getLogger(EmailValidator.class);

    /**
     * This method validates an email template.
     * It checks if the email body, placeholders, and tokens are not null,
     * and if the number of distinct tokens matches the number of placeholders.
     *
     * @param emailBody the body of the email template
     * @param placeholders the placeholders in the email template
     * @param tokens the tokens to replace the placeholders with
     * @return true if the email template is valid, false otherwise
     */
    public boolean isValid(String emailBody, List<String> placeholders, List<String> tokens) {
        long uniqueTokens = tokens.stream().distinct().count();

        if (ObjectUtils.anyNull(emailBody, placeholders, tokens) || placeholders.size() != uniqueTokens) {
            log.error("Invalid Email Format: emailBody, placeholders, tokens cannot be null, and/or tokens do not match the number of placeholders");
            return false;
        }

        return true;
    }

}
