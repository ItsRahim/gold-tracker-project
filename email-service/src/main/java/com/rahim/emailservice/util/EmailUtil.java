package com.rahim.emailservice.util;

import com.rahim.emailservice.exception.EmailTemplateException;
import com.rahim.emailservice.model.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a utility that populates email templates with tokens.
 * It uses the EmailValidator to validate the email template,
 * and the ITokenMapPopulator to populate a map with placeholders and tokens.
 *
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
@Component
@RequiredArgsConstructor
public class EmailUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EmailUtil.class);
    private final EmailValidator emailValidator;
    private final ITokenMapPopulator tokenMapPopulator;

    /**
     * This method replaces placeholders in a string with the corresponding tokens from a map.
     *
     * @param input the string with placeholders
     * @param replacementMap the map with placeholder-token pairs
     * @return the input string with placeholders replaced by tokens
     */
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

    /**
     * This method populates an email template with tokens.
     * It validates the email template, populates a map with placeholders and tokens,
     * and replaces the placeholders in the email template with the tokens.
     *
     * @param emailTemplate the email template to populate
     * @param placeholders the placeholders to replace in the email template
     * @param tokens the tokens to replace the placeholders with
     * @return the populated email template
     * @throws EmailTemplateException if the email template is null or invalid
     */
    public EmailTemplate populateTemplate(EmailTemplate emailTemplate, List<String> placeholders, List<String> tokens) {
        if (emailTemplate == null) {
            LOG.warn("Email template is null");
            throw new EmailTemplateException("Email template is null");
        }

        LOG.debug("Populating email template with ID: {} - {}", emailTemplate.getId(), emailTemplate.getTemplateName());

        boolean isValidEmail = emailValidator.isValid(emailTemplate.getBody(), placeholders, tokens);

        if (!isValidEmail) {
            LOG.warn("Invalid information provided to populate email");
            throw new EmailTemplateException("Invalid information provided to populate email");
        }

        LOG.debug("Information provided is sufficient to populate email");

        tokenMapPopulator.populateTokenMap(placeholders, tokens);

        String replacedBody = replacePlaceholders(emailTemplate.getBody(), tokenMapPopulator.getPlaceholderTokenMap());
        replacedBody = replacedBody.replace("\\n", "\n");
        emailTemplate.setBody(replacedBody);

        return emailTemplate;
    }
}
