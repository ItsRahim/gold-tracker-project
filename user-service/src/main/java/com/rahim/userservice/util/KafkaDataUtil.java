package com.rahim.userservice.util;

import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.kafka.IKafkaService;
import com.rahim.userservice.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@RequiredArgsConstructor
public class KafkaDataUtil {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaDataUtil.class);
    private final IKafkaService kafkaService;
    private final IUserProfileService userProfileService;

    public void prepareEmailData(TemplateNameEnum templateName, int userId) {
        switch (templateName) {
            case ACCOUNT_DELETED:
                sendDataToKafka(templateName, userId, true, false);
                break;
            case ACCOUNT_DELETION:
                sendDataToKafka(templateName, userId, true, true);
                break;
            case ACCOUNT_INACTIVITY:
                sendDataToKafka(templateName, userId, false, false);
                break;
            case ACCOUNT_UPDATE:
                sendDataToKafka(templateName, userId, false, true);
                break;
            default:
                LOG.warn("Unhandled template name: {}", templateName);
                break;
        }
    }

    private void sendDataToKafka(TemplateNameEnum templateName, int userId, boolean includeUsername, boolean includeDate) {
        try {
            String template = templateName.toString();
            String tokenData = userProfileService.getEmailTokens(template, userId, includeUsername, includeDate).toString();
            kafkaService.sendMessage("email-service-send-email", tokenData);
            LOG.info("Data sent to Kafka topic 'email-service-send-email' for user ID: {}", userId);
        } catch (Exception e) {
            LOG.error("Error preparing and sending data to Kafka: {}", e.getMessage(), e);
        }
    }
}
