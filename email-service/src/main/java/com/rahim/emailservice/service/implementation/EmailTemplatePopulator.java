package com.rahim.emailservice.service.implementation;

import com.rahim.emailservice.exception.EmailTemplateNotFoundException;
import com.rahim.emailservice.model.EmailTemplate;
import com.rahim.emailservice.repository.EmailTemplateRepository;
import com.rahim.emailservice.service.IEmailTemplatePopulator;
import com.rahim.emailservice.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is a service that populates email templates.
 * It implements the IEmailTemplatePopulator interface.
 * It uses the EmailTemplateRepository to fetch email templates and placeholders,
 * and the EmailUtil to populate the templates with the provided tokens.
 *
 * @author Rahim Ahmed
 * @created  18/12/2023
 */
@Service
@RequiredArgsConstructor
public class EmailTemplatePopulator implements IEmailTemplatePopulator {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTemplatePopulator.class);
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailUtil emailUtil;

    @Override
    public EmailTemplate populateTemplate(int templateId, List<String> tokens) {
        try {
            EmailTemplate emailTemplate = emailTemplateRepository.findById(templateId)
                    .orElseThrow(() -> new EmailTemplateNotFoundException("Email template with ID " + templateId + " not found"));

            List<String> placeholders = emailTemplateRepository.findPlaceholdersByTemplateId(templateId);

            return emailUtil.populateTemplate(emailTemplate, placeholders, tokens);
        } catch (Exception e) {
            LOG.error("An error has occurred attempting to populate the email placeholder", e);
            throw new RuntimeException(e);
        }
    }
}