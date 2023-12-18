package com.rahim.emailservice.service;

import com.rahim.emailservice.model.EmailTemplate;

import java.util.List;

public interface IEmailService {
    void sendEmail(String recipientEmail);
    EmailTemplate populateTemplate(int templateId, List<String> tokens);
    void processKafkaData(String emailData);
}
