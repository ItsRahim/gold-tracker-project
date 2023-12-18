package com.rahim.emailservice.service;

import com.rahim.emailservice.model.EmailTemplate;

import java.util.List;

public interface IEmailTemplatePopulator {
    EmailTemplate populateTemplate(int templateId, List<String> tokens);
}
