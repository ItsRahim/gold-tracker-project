package com.rahim.emailservice.service.implementation;

import com.rahim.emailservice.model.EmailTemplate;
import com.rahim.emailservice.repository.EmailHistoryRepository;
import com.rahim.emailservice.repository.EmailTemplateRepository;
import com.rahim.emailservice.service.IEmailService;
import com.rahim.emailservice.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements IEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImplementation.class);
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailHistoryRepository emailHistoryRepository;
    private final EmailUtil emailUtil;

    @Override
    public void sendEmail(String recipientEmail) {
        // Implement your sendEmail logic here
    }

    @Override
    public EmailTemplate populateTemplate(int templateId, List<String> tokens) {
        try {
            Optional<EmailTemplate> emailTemplateOptional = emailTemplateRepository.findById(templateId);
            if (emailTemplateOptional.isPresent()) {
                EmailTemplate emailTemplate = emailTemplateOptional.get();
                return emailUtil.populateTemplate(emailTemplate, tokens);
            } else {
                LOG.warn("Email template with ID {} not found", templateId);
            }
        } catch (Exception e) {
            LOG.error("An error has occurred attempting to populate the email placeholder", e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
