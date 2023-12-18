package com.rahim.userservice.util.implementation;

import com.rahim.userservice.util.IEmailTokenService;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.kafka.IKafkaService;
import com.rahim.userservice.util.IMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailTokenService implements IEmailTokenService{
    private static final Logger LOG = LoggerFactory.getLogger(EmailTokenService.class);
    private final IMessageFormatter messageFormatter;
    private final IKafkaService kafkaService;
    private static final String SEND_EMAIL_TOPIC = "email-service-send-email";

    @Override
    public void generateEmailTokens(Map<String, Object> emailData, String templateName, boolean includeUsername, boolean includeDate) {
        try {
            Map<String, Object> mutableEmailData = new HashMap<>(emailData);

            messageFormatter.updateMapKey(mutableEmailData, "first_name", "firstName");
            messageFormatter.updateMapKey(mutableEmailData, "last_name", "lastName");
            messageFormatter.updateMapKey(mutableEmailData, "delete_date", "deleteDate");
            messageFormatter.updateMapKey(mutableEmailData, "updated_at", "updatedAt");

            if (!includeUsername) {
                mutableEmailData.remove("username");
            }

            if (!includeDate) {
                List<String> keysToRemove = Arrays.asList("deleteDate", "updatedAt");
                mutableEmailData.keySet().removeAll(keysToRemove);
            }

            if (TemplateNameEnum.ACCOUNT_DELETION.getTemplateName().equals(templateName) && includeDate) {
                mutableEmailData.remove("updatedAt");
                messageFormatter.formatInstant(mutableEmailData, "deleteDate");
            }

            if (TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName().equals(templateName) && includeDate) {
                mutableEmailData.remove("deleteDate");
                messageFormatter.formatInstant(mutableEmailData, "updatedAt");
            }

            mutableEmailData.put("templateName", templateName);

            if (ObjectUtils.anyNull(mutableEmailData)) {
                LOG.error("Email token values are null. Unable to generate email tokens");
                throw new RuntimeException("Email token values are null. Unable to generate email tokens");
            }

            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            String jsonEmailData = objectMapper.writeValueAsString(mutableEmailData);

            LOG.trace("Generated tokens: {}", jsonEmailData);

            kafkaService.sendMessage(SEND_EMAIL_TOPIC, jsonEmailData);
        } catch (Exception e) {
            LOG.error("Error generating email tokens: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error", e);
        }
    }
}
