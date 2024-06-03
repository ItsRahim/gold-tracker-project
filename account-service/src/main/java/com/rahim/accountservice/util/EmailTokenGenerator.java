package com.rahim.accountservice.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.model.EmailToken;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.IKafkaService;
import com.rahim.common.util.KafkaKeyUtil;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTokenGenerator {

    private final IProfileRepositoryHandler profileRepositoryHandler;
    private final IKafkaService kafkaService;

    public void generateEmailTokens(EmailProperty emailProperty) {
        try {
            EmailToken emailToken = profileRepositoryHandler.generateEmailTokens(emailProperty);
            String jsonEmailData = KafkaKeyUtil.generateKeyWithUUID(convertToJson(emailToken));
            log.debug("Generated tokens: {}", jsonEmailData);
            kafkaService.sendMessage(KafkaTopic.SEND_EMAIL, jsonEmailData);
        } catch (JsonProcessingException e) {
            handleException("Error converting tokens to JSON", e);
        } catch (ValidationException e) {
            handleException("Validation failed for email data", e);
        } catch (Exception e) {
            handleException("Error generating email tokens", e);
        }
    }

    private String convertToJson(EmailToken emailToken) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(emailToken);
    }

    private void handleException(String message, Exception e) {
        log.error("{}: {}", message, e.getMessage(), e);
        throw new RuntimeException(message, e);
    }
}
