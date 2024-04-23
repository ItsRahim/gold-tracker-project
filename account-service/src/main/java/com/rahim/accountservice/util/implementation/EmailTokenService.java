package com.rahim.accountservice.util.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.accountservice.constant.EmailTemplate;
import com.rahim.accountservice.constant.TopicConstants;
import com.rahim.accountservice.kafka.IKafkaService;
import com.rahim.accountservice.request.AccountJsonRequest;
import com.rahim.accountservice.request.ProfileJsonRequest;
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

/**
 * Author: Rahim Ahmed
 * Created: 18/12/2023
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

    private Map<String, Object> prepareEmailData(Map<String, Object> emailData, boolean includeUsername, boolean includeDate, String templateName) {
        Map<String, Object> mutableEmailData = new HashMap<>(emailData);
        updateKeysAndRemoveFields(mutableEmailData, includeUsername, includeDate, templateName);
        return mutableEmailData;
    }

    private void updateKeysAndRemoveFields(Map<String, Object> mutableEmailData, boolean includeUsername, boolean includeDate, String templateName) {
        updateKeys(mutableEmailData);
        if (!includeUsername) {
            mutableEmailData.remove(ProfileJsonRequest.PROFILE_USERNAME);
        }
        if (!includeDate) {
            List<String> keysToRemove = Arrays.asList(AccountJsonRequest.ACCOUNT_DELETE_DATE, AccountJsonRequest.ACCOUNT_UPDATED_AT);
            mutableEmailData.keySet().removeAll(keysToRemove);
        }
        handleTemplateName(templateName, mutableEmailData);
    }

    private void updateKeys(Map<String, Object> mutableEmailData) {
        messageFormatter.updateMapKey(mutableEmailData, "first_name", ProfileJsonRequest.PROFILE_FIRST_NAME);
        messageFormatter.updateMapKey(mutableEmailData, "last_name", ProfileJsonRequest.PROFILE_LAST_NAME);
        messageFormatter.updateMapKey(mutableEmailData, "delete_date", AccountJsonRequest.ACCOUNT_DELETE_DATE);
        messageFormatter.updateMapKey(mutableEmailData, "updated_at", AccountJsonRequest.ACCOUNT_UPDATED_AT);
    }

    private void handleTemplateName(String templateName, Map<String, Object> mutableEmailData) {
        if (EmailTemplate.ACCOUNT_DELETION_TEMPLATE.equals(templateName)) {
            mutableEmailData.remove(AccountJsonRequest.ACCOUNT_UPDATED_AT);
            messageFormatter.formatInstant(mutableEmailData, AccountJsonRequest.ACCOUNT_DELETE_DATE);
        } else if (EmailTemplate.ACCOUNT_UPDATE_TEMPLATE.equals(templateName)) {
            mutableEmailData.remove(AccountJsonRequest.ACCOUNT_DELETE_DATE);
            messageFormatter.formatInstant(mutableEmailData, AccountJsonRequest.ACCOUNT_UPDATED_AT);
        }
        mutableEmailData.put("templateName", templateName);
    }

    private void validateEmailData(Map<String, Object> mutableEmailData) {
        if (ObjectUtils.anyNull(mutableEmailData)) {
            LOG.error("Email token values are null. Unable to generate email tokens");
            throw new RuntimeException("Email token values are null. Unable to generate email tokens");
        }
    }

    private String convertToJson(Map<String, Object> mutableEmailData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(mutableEmailData);
    }

}
