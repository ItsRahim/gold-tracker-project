package com.rahim.emailservice.service.implementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.emailservice.model.EmailTemplate;
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

@Service
@RequiredArgsConstructor
public class KafkaDataProcessor implements IKafkaDataProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaDataProcessor.class);
    private final IEmailTemplatePopulator emailTemplatePopulator;
    private final IEmailService emailService;
    private final IEmailSender emailSender;

    @Override
    public void processKafkaData(String emailData) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> kafkaData = objectMapper.readValue(emailData, new TypeReference<>() {});

            String templateName = (String) kafkaData.get("templateName");
            String email = (String) kafkaData.get("email");

            kafkaData.remove("templateName");
            kafkaData.remove("email");

            List<String> emailTokens = new ArrayList<>();
            for (Map.Entry<String, Object> entry : kafkaData.entrySet()) {
                emailTokens.add(entry.getValue().toString());
            }

            Integer templateId = emailService.findIdByName(templateName);
            EmailTemplate emailTemplate = emailTemplatePopulator.populateTemplate(templateId, emailTokens);
            emailSender.sendEmail(email, emailTemplate);

        } catch (Exception e) {
            LOG.error("Error processing Kafka data: {}", e.getMessage(), e);
        }
    }
}
