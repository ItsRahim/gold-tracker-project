package com.rahim.emailservice.service;

import com.rahim.emailservice.model.EmailTemplate;

public interface IEmailSender {
    void sendEmail(String recipientEmail, EmailTemplate emailContent);
}
