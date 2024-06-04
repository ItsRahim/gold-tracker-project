package com.rahim.notificationservice.service.kafka.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.IKafkaService;
import com.rahim.common.util.DateTimeUtil;
import com.rahim.common.util.JsonUtil;
import com.rahim.notificationservice.model.EmailData;
import com.rahim.notificationservice.model.NotificationResult;
import com.rahim.notificationservice.repository.ThresholdAlertRepository;
import com.rahim.notificationservice.service.kafka.IKafkaDataProcessor;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaDataProcessor implements IKafkaDataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaDataProcessor.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private final ThresholdAlertRepository thresholdAlertRepository;
    private final IKafkaService kafkaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processKafkaData(String priceData) {
        try {
            BigDecimal currentPrice = new BigDecimal(priceData);
            List<NotificationResult> notificationList = thresholdAlertRepository.generateEmailTokens(currentPrice);
            LOG.debug("Retrieved {} notification records from the database for current price: {}", notificationList.size(), currentPrice);

            processNotifications(notificationList);
        } catch (NumberFormatException e) {
            LOG.error("Error parsing price data. Invalid format: {}", priceData);
        } catch (DataAccessException e) {
            LOG.error("Error accessing data from the database", e);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred", e);
        }
    }

    private void processNotifications(List<NotificationResult> notificationList) {
        notificationList.forEach(this::processNotification);
    }

    private void processNotification(NotificationResult notificationResult) {
        thresholdAlertRepositoryHandler.deactivateAlert(notificationResult.getAlertId());
        LOG.info("Deactivated alert for user: {}", notificationResult.getEmail());

        EmailData emailData = createEmailData(notificationResult);
        sendEmail(emailData);
    }

    private EmailData createEmailData(NotificationResult notificationResult) {
        return EmailData.builder()
                .firstName(notificationResult.getFirstName())
                .lastName(notificationResult.getLastName())
                .email(notificationResult.getEmail())
                .thresholdPrice(String.valueOf(notificationResult.getThresholdPrice()))
                .alertDateTime(DateTimeUtil.getFormattedTime())
                .emailTemplate(EmailTemplate.PRICE_ALERT_TEMPLATE)
                .build();
    }

    private void sendEmail(EmailData emailData) {
        String jsonEmailData = JsonUtil.convertObjectToJson(emailData);
        kafkaService.sendMessage(KafkaTopic.SEND_EMAIL, jsonEmailData);
    }
}
