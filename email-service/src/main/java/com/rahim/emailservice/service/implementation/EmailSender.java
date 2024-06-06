package com.rahim.emailservice.service.implementation;

import com.rahim.emailservice.entity.EmailTemplate;
import com.rahim.emailservice.service.IEmailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * This class is a service that sends emails.
 * It implements the IEmailSender interface.
 * It uses Spring's JavaMailSender to send emails.
 *
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
@Service
@RefreshScope
@RequiredArgsConstructor
public class EmailSender implements IEmailSender {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String recipientEmail, EmailTemplate emailContent) {
        log.debug("Attempting to send email...");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(recipientEmail);
        message.setSubject(emailContent.getSubject());
        message.setText(emailContent.getBody());

        javaMailSender.send(message);
        log.info("Email sent successfully");
    }
}