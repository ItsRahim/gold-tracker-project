package com.rahim.emailservice.service;

import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.common.model.kafka.PriceAlertEmailData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * @author Rahim Ahmed
 * @created 26/11/2023
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final EmailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendAccountAlert(AccountEmailData accountData) {
        log.debug("Processing account alert email data");
        sendEmail(accountData.getEmail(), accountData.getEmailTemplate(), accountData);
    }

    public void sendPriceAlert(PriceAlertEmailData priceAlert) {
        log.debug("Processing price alert email data");
        sendEmail(priceAlert.getEmail(), priceAlert.getEmailTemplate(), priceAlert);
    }

    private void sendEmail(String recipientEmail, EmailTemplate template, Object emailData) {
        String subject = template.getSubject();
        Context context = new Context();
        context.setVariable("emailData", emailData);
        String emailContent = templateEngine.process(template.getTemplateFileName(), context);
        emailSender.sendEmail(recipientEmail, emailContent, subject);
    }
}