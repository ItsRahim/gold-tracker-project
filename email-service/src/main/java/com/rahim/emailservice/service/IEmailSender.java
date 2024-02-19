package com.rahim.emailservice.service;

import com.rahim.emailservice.model.EmailTemplate;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
public interface IEmailSender {

    /**
     * This method sends an email to the specified recipient with the specified content.
     * It creates a SimpleMailMessage, sets the sender, recipient, subject, and text,
     * and then sends the email using JavaMailSender.
     *
     * @param recipientEmail the email address of the recipient
     * @param emailContent the content of the email
     */
    void sendEmail(String recipientEmail, EmailTemplate emailContent);
}
