package com.rahim.emailservice.util;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@NoArgsConstructor
@Component
public class TemplateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(TemplateUtil.class);
    private String emailBody;
    private List<String> placeholder;
    private List<String> tokens;
    private boolean isValid;

    private TemplateUtil(String emailBody, List<String> placeholder, List<String> tokens) {
        this.emailBody = emailBody;
        this.placeholder = placeholder;
        this.tokens = tokens;

        this.isValid = validateNotNull();
    }

    public static TemplateUtil create(String emailBody, List<String> placeholder, List<String> tokens) {
        return new TemplateUtil(emailBody, placeholder, tokens);
    }

    public boolean isValid() {
        return isValid;
    }

    private boolean validateNotNull() {
        if (ObjectUtils.anyNull(emailBody, placeholder, tokens) || placeholder.size() != tokens.size()) {
            LOG.warn("Invalid Email Template: emailBody, placeholder, tokens cannot be null, and placeholder and tokens must have the same size.");
            return false;
        }
        return true;
    }
}
