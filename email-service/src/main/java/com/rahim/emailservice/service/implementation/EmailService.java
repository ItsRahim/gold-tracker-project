package com.rahim.emailservice.service.implementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.emailservice.model.EmailTemplate;
import com.rahim.emailservice.repository.EmailTemplateRepository;
import com.rahim.emailservice.service.IEmailService;
import com.rahim.emailservice.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailUtil emailUtil;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String fromEmail;

    @Override
    public void sendEmail(String recipientEmail, EmailTemplate emailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(recipientEmail);
        message.setSubject(emailContent.getSubject());
        message.setText(emailContent.getBody());

        javaMailSender.send(message);
    }

    @Override
    public EmailTemplate populateTemplate(int templateId, List<String> tokens) {
        try {
            EmailTemplate emailTemplate = emailTemplateRepository.findById(templateId)
                    .orElseThrow(() -> new RuntimeException("Email template with ID " + templateId + " not found"));

            List<String> placeholders = emailTemplateRepository.findPlaceholdersByTemplateId(templateId);

            return emailUtil.populateTemplate(emailTemplate, placeholders, tokens);
        } catch (Exception e) {
            LOG.error("An error has occurred attempting to populate the email placeholder", e);
            throw new RuntimeException(e);
        }
    }

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
            Integer templateId = findIdByName(templateName);
            EmailTemplate emailTemplate = populateTemplate(templateId, emailTokens);
            sendEmail(email, emailTemplate);

        } catch (Exception e) {
            LOG.error("Error processing Kafka data: {}", e.getMessage(), e);
        }
    }

    private Integer findIdByName(String templateName) {
        try {
            LOG.info("Attempting to find ID for template name: {}", templateName);

            Integer id = emailTemplateRepository.findIdByTemplateName(templateName);

            if (id != null) {
                LOG.info("ID found for template name {}: {}", templateName, id);
            } else {
                LOG.info("No ID found for template name: {}", templateName);
            }

            return id;
        } catch (Exception e) {
            LOG.error("Error finding ID for template name {}: {}", templateName, e.getMessage(), e);
            throw new RuntimeException("Error finding ID for template name", e);
        }
    }
}