package com.rahim.emailservice.service;

import com.rahim.emailservice.model.EmailTemplate;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
public interface IEmailTemplatePopulator {

    /**
     * This method populates an email template with the provided tokens.
     * It fetches the email template and its placeholders from the repository,
     * and then uses the EmailUtil to replace the placeholders with the tokens.
     *
     * @param templateId the ID of the email template to populate
     * @param tokens the tokens to replace the placeholders in the template
     * @return the populated email template
     * @throws EmailTemplateNotFoundException if the email template with the provided ID is not found
     */
    EmailTemplate populateTemplate(int templateId, List<String> tokens);
}
