package com.rahim.emailservice.service.implementation;

import com.rahim.emailservice.repository.EmailTemplateRepository;
import com.rahim.emailservice.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    public Integer findIdByName(String templateName) {
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