package com.rahim.emailservice.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.emailservice.entity.EmailTemplate;
import com.rahim.emailservice.service.IEmailSender;
import com.rahim.emailservice.service.IEmailService;
import com.rahim.emailservice.service.IEmailTemplatePopulator;
import com.rahim.emailservice.service.IKafkaDataProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is a service that processes Kafka data.
 * It implements the IKafkaDataProcessor interface.
 * It uses the IEmailTemplatePopulator, IEmailService, and IEmailSender to process the data and send emails.
 *
 * @author Rahim Ahmed
 * @since 18/12/2024
 */
@Service
@RequiredArgsConstructor
public class KafkaDataProcessor implements IKafkaDataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaDataProcessor.class);
    private final IEmailTemplatePopulator emailTemplatePopulator;
    private final IEmailService emailService;
    private final IEmailSender emailSender;

    private static final String EMAIL_TEMPLATE = "emailTemplate";
    private static final String EMAIL = "email";

    @Override
    public void processKafkaData(String emailData) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> kafkaData = objectMapper.readValue(emailData, new TypeReference<>() {});

            String templateName = (String) kafkaData.get(EMAIL_TEMPLATE);
            String email = (String) kafkaData.get(EMAIL);

            kafkaData.remove(EMAIL_TEMPLATE);
            kafkaData.remove(EMAIL);

            List<String> emailTokens = new ArrayList<>();
            for (Map.Entry<String, Object> entry : kafkaData.entrySet()) {
                emailTokens.add(entry.getValue().toString());
            }

            Integer templateId = emailService.findIdByName(templateName);
            EmailTemplate emailTemplate = emailTemplatePopulator.populateTemplate(templateId, emailTokens);
            emailSender.sendEmail(email, emailTemplate);

        } catch (JsonProcessingException e) {
            LOG.error("Error processing Kafka data: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing Kafka data", e);
        }
    }
}
