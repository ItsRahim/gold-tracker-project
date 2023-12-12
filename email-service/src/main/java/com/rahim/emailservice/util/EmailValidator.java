package com.rahim.emailservice.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailValidator {

    private static final Logger LOG = LoggerFactory.getLogger(EmailValidator.class);

    public boolean isValid(String emailBody, List<String> placeholders, List<String> tokens) {
        long uniqueElements = tokens.stream().distinct().count();
        if (ObjectUtils.anyNull(emailBody, placeholders, tokens) || placeholders.size() != uniqueElements) {
            LOG.warn("Invalid Email Format: emailBody, placeholders, tokens cannot be null, and/or tokens do not match the number of placeholders");
            return false;
        }
        return true;
    }
}
