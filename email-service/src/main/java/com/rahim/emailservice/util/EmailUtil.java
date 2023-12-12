package com.rahim.emailservice.util;

import com.rahim.emailservice.model.EmailTemplate;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Setter
@RequiredArgsConstructor
public class EmailUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EmailUtil.class);
    private final EmailValidator emailValidator;
    private final ITokenMapPopulator tokenMapPopulator;

    public String replacePlaceholders(String input, Map<String, String> replacementMap) {
        String regex = "@\\w+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String placeholder = matcher.group();
            String replacement = replacementMap.getOrDefault(placeholder, placeholder);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public EmailTemplate populateTemplate(EmailTemplate emailTemplate, List<String> tokens) {
        if (emailTemplate != null) {
            LOG.info("Populating email template with ID: {} - {}", emailTemplate.getId(), emailTemplate.getTemplateName());

            boolean isValidEmail = emailValidator.isValid(emailTemplate.getBody(), emailTemplate.getPlaceholders(), tokens);

            if (isValidEmail) {
                LOG.info("Information provided is sufficient to populate email");

                tokenMapPopulator.populateTokenMap(emailTemplate.getPlaceholders(), tokens);

                String replacedBody = replacePlaceholders(emailTemplate.getBody(), tokenMapPopulator.getPlaceholderTokenMap());
                emailTemplate.setBody(replacedBody);
                return emailTemplate;
            } else {
                LOG.warn("Invalid information provided to populate email");
            }
        } else {
            LOG.warn("Email template is null");
        }

        return null;
    }
}