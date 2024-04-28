package com.rahim.accountservice.util.implementation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.accountservice.constant.TopicConstants;
import com.rahim.accountservice.kafka.IKafkaService;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.model.EmailToken;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.accountservice.util.IEmailTokenService;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailTokenService implements IEmailTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTokenService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;

    @Override
    public void generateEmailTokens(EmailProperty emailProperty) {
        try {
            EmailToken emailToken = profileRepositoryHandler.generateEmailTokens(emailProperty);
            String jsonEmailData = convertToJson(emailToken);
            LOG.trace("Generated tokens: {}", jsonEmailData);
            kafkaService.sendMessage(topicConstants.getSendEmailTopic(), jsonEmailData);
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
        LOG.error("{}: {}", message, e.getMessage(), e);
        throw new RuntimeException(message, e);
    }
}
