package com.rahim.userservice.service.implementation;

import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.kafka.IKafkaService;
import com.rahim.userservice.service.IEmailService;
import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final IUserService userService;
    private final IUserProfileService userProfileService;
    private final IKafkaService kafkaService;

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Override
    public void generateEmailData(TemplateNameEnum templateName, int userId) {
        try {
            Map<String, Object> userProfileData = getUserProfileData(templateName, userId);
            Map<String, Object> userData = getUserData(templateName, userId);

            if (userProfileData == null || userData == null) {
                LOG.warn("Unable to generate email data for user {} with template {}", userId, templateName);
                return;
            }

            Map<String, Object> combinedData = new HashMap<>(userProfileData);
            combinedData.putAll(userData);

            String emailData = combinedData.toString();

            LOG.info("Generated email data for user {} with template {}: {}", userId, templateName, emailData);

            kafkaService.sendMessage("email-service-send-email", emailData);

            LOG.info("Sent email data for user {} with template {}", userId, templateName);
        } catch (Exception e) {
            LOG.error("Error generating and sending email data for user {} with template {}", userId, templateName, e);
            throw new RuntimeException("Error generating and sending email data", e);
        }
    }


    private Map<String, Object> getUserProfileData(TemplateNameEnum templateName, int userId) {
        boolean includeUsername = true;

        switch (templateName) {
            case ACCOUNT_DELETION:
            case ACCOUNT_DELETED:
            case ACCOUNT_UPDATE:
                break;
            case ACCOUNT_INACTIVITY:
                includeUsername = false;
                break;
            default:
                return null;
        }

        String template = templateName.toString();
        return userProfileService.getEmailTokens(template, userId, includeUsername);
    }

    private Map<String, Object> getUserData(TemplateNameEnum templateName, int userId) {
        return userService.getEmailToken(userId, templateName);
    }
}
