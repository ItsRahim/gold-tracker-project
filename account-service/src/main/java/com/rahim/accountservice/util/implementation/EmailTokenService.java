package com.rahim.accountservice.util.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.accountservice.constant.TopicConstants;
import com.rahim.accountservice.util.IEmailTokenService;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.accountservice.enums.TemplateNameEnum;
import com.rahim.accountservice.kafka.IKafkaService;
import com.rahim.accountservice.util.IMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

            LOG.trace("Generated tokens: {}", jsonEmailData);

            kafkaService.sendMessage(topicConstants.getSendEmailTopic(), jsonEmailData);
        } catch (Exception e) {
            handleException(e);
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
            mutableEmailData.remove("username");
        }

        if (!includeDate) {
            List<String> keysToRemove = Arrays.asList("deleteDate", "updatedAt");
            mutableEmailData.keySet().removeAll(keysToRemove);
        }

        handleTemplateName(templateName, mutableEmailData);
    }

    private void updateKeys(Map<String, Object> mutableEmailData) {
        messageFormatter.updateMapKey(mutableEmailData, "first_name", "firstName");
        messageFormatter.updateMapKey(mutableEmailData, "last_name", "lastName");
        messageFormatter.updateMapKey(mutableEmailData, "delete_date", "deleteDate");
        messageFormatter.updateMapKey(mutableEmailData, "updated_at", "updatedAt");
    }

    private void handleTemplateName(String templateName, Map<String, Object> mutableEmailData) {
        if (TemplateNameEnum.ACCOUNT_DELETION.getTemplateName().equals(templateName)) {
            mutableEmailData.remove("updatedAt");
            messageFormatter.formatInstant(mutableEmailData, "deleteDate");
        } else if (TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName().equals(templateName)) {
            mutableEmailData.remove("deleteDate");
            messageFormatter.formatInstant(mutableEmailData, "updatedAt");
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

    private void handleException(Exception e) {
        LOG.error("Error generating email tokens: {}", e.getMessage(), e);
        throw new RuntimeException("Unexpected error", e);
    }
}
