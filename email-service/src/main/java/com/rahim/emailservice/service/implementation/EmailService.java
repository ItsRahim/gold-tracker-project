package com.rahim.emailservice.service.implementation;

import com.rahim.emailservice.repository.EmailTemplateRepository;
import com.rahim.emailservice.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 26/11/2023
 */
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    public Integer findIdByName(String templateName) {
        try {
            log.debug("Attempting to find ID for template name: {}", templateName);
            return emailTemplateRepository.findIdByTemplateName(templateName);
        } catch (Exception e) {
            log.error("Error finding ID for template name {}: {}", templateName, e.getMessage(), e);
            throw new RuntimeException("Error finding ID for template name", e);
        }
    }
}