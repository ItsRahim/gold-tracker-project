package com.rahim.emailservice.service.implementation;

import com.rahim.emailservice.model.EmailTemplate;
import com.rahim.emailservice.service.IEmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender implements IEmailSender {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String fromEmail;

    @Override
    public void sendEmail(String recipientEmail, EmailTemplate emailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(recipientEmail);
        message.setSubject(emailContent.getSubject());
        message.setText(emailContent.getBody());

        javaMailSender.send(message);
    }
}