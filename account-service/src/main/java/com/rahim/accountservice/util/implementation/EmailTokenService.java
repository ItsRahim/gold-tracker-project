package com.rahim.accountservice.util.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.accountservice.constant.TopicConstants;
import com.rahim.accountservice.kafka.IKafkaService;
import com.rahim.accountservice.util.IEmailTokenService;
import com.rahim.accountservice.util.IMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rahim.accountservice.constant.EmailTemplates.*;
import static com.rahim.accountservice.model.request.AccountRequestParam.*;
import static com.rahim.accountservice.model.request.ProfileRequestParam.*;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
@Service
@RequiredArgsConstructor
public class EmailTokenService implements IEmailTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTokenService.class);
    private final IMessageFormatter messageFormatter;
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;

    @Override
    public void generateEmailTokens(Map<String, Object> emailData, String templateName, boolean includeUsername, boolean includeDate) {
        try {
            Map<String, Object> mutableEmailData = prepareEmailData(emailData, includeUsername, includeDate, templateName);

            validateEmailData(mutableEmailData);

            String jsonEmailData = convertToJson(mutableEmailData);

            LOG.debug("Generated tokens: {}", jsonEmailData);

            kafkaService.sendMessage(topicConstants.getSendEmailTopic(), jsonEmailData);
        } catch (Exception e) {
            LOG.error("Error generating email tokens: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error", e);
        }
    }

    /**
     * Prepares the email data by updating keys, removing fields based on the parameters, and handling the template name.
     *
     * @param emailData The original map containing the email data.
     * @param includeUsername A flag indicating whether to include the username in the map.
     * @param includeDate A flag indicating whether to include the date in the map.
     * @param templateName The name of the email template.
     * @return A new map containing the prepared email data.
     */
    private Map<String, Object> prepareEmailData(Map<String, Object> emailData, boolean includeUsername, boolean includeDate, String templateName) {
        Map<String, Object> mutableEmailData = new HashMap<>(emailData);

        updateKeysAndRemoveFields(mutableEmailData, includeUsername, includeDate, templateName);

        return mutableEmailData;
    }

    /**
     * Updates the keys in the mutable email data map, removes fields based on the parameters, and handles the template name.
     *
     * @param mutableEmailData The map containing the email data to be updated.
     * @param includeUsername A flag indicating whether to include the username in the map.
     * @param includeDate A flag indicating whether to include the date in the map.
     * @param templateName The name of the email template.
     */
    private void updateKeysAndRemoveFields(Map<String, Object> mutableEmailData, boolean includeUsername, boolean includeDate, String templateName) {
        updateKeys(mutableEmailData);

        if (!includeUsername) {
            mutableEmailData.remove(PROFILE_USERNAME);
        }

        if (!includeDate) {
            List<String> keysToRemove = Arrays.asList(ACCOUNT_DELETE_DATE, ACCOUNT_UPDATED_AT);
            mutableEmailData.keySet().removeAll(keysToRemove);
        }

        handleTemplateName(templateName, mutableEmailData);
    }

    /**
     * Updates the keys in the given mutable email data map.
     *
     * @param mutableEmailData The map containing the email data with keys to be updated.
     */
    private void updateKeys(Map<String, Object> mutableEmailData) {
        messageFormatter.updateMapKey(mutableEmailData, "first_name", PROFILE_FIRST_NAME);
        messageFormatter.updateMapKey(mutableEmailData, "last_name", PROFILE_LAST_NAME);
        messageFormatter.updateMapKey(mutableEmailData, "delete_date", ACCOUNT_DELETE_DATE);
        messageFormatter.updateMapKey(mutableEmailData, "updated_at", ACCOUNT_UPDATED_AT);
    }

    /**
     * Updates the email token map according to the email template being used
     *
     * @param templateName The name of the email template.
     * @param mutableEmailData The map containing the email data to be updated.
     */
    private void handleTemplateName(String templateName, Map<String, Object> mutableEmailData) {
        if (ACCOUNT_DELETION_TEMPLATE.equals(templateName)) {
            mutableEmailData.remove(ACCOUNT_UPDATED_AT);
            messageFormatter.formatInstant(mutableEmailData, ACCOUNT_DELETE_DATE);
        } else if (ACCOUNT_UPDATE_TEMPLATE.equals(templateName)) {
            mutableEmailData.remove(ACCOUNT_DELETE_DATE);
            messageFormatter.formatInstant(mutableEmailData, ACCOUNT_UPDATED_AT);
        }

        mutableEmailData.put("templateName", templateName);
    }

    /**
     * Validates the mutable email data map.
     *
     * @param mutableEmailData The map containing the email data to be validated.
     * @throws RuntimeException If any of the email token values are null.
     */
    private void validateEmailData(Map<String, Object> mutableEmailData) {
        if (ObjectUtils.anyNull(mutableEmailData)) {
            LOG.error("Email token values are null. Unable to generate email tokens");
            throw new RuntimeException("Email token values are null. Unable to generate email tokens");
        }
    }

    /**
     * Converts the mutable email data map to a JSON string to be sent via Kafka
     *
     * @param mutableEmailData The map containing the email data to be converted.
     * @return A JSON string representation of the email data.
     * @throws JsonProcessingException If an error occurs during the conversion.
     */
    private String convertToJson(Map<String, Object> mutableEmailData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(mutableEmailData);
    }

}
