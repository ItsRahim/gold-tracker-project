package com.rahim.notificationservice.service.kafka.implementation;

import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.IKafkaService;
import com.rahim.common.util.DateTimeUtil;
import com.rahim.common.util.JsonUtil;
import com.rahim.common.model.kafka.PriceAlertEmailData;
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

    private static final Logger log = LoggerFactory.getLogger(KafkaDataProcessor.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private final ThresholdAlertRepository thresholdAlertRepository;
    private final IKafkaService kafkaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processKafkaData(String priceData) {
        try {
            BigDecimal currentPrice = new BigDecimal(priceData);
            List<NotificationResult> notificationList = thresholdAlertRepository.generateEmailTokens(currentPrice);
            log.debug("Retrieved {} notification records from the database for current price: {}", notificationList.size(), currentPrice);

            processNotifications(notificationList);
        } catch (NumberFormatException e) {
            log.error("Error parsing price data. Invalid format: {}", priceData);
        } catch (DataAccessException e) {
            log.error("Error accessing data from the database", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred", e);
        }
    }

    private void processNotifications(List<NotificationResult> notificationList) {
        notificationList.forEach(this::processNotification);
    }

    private void processNotification(NotificationResult notificationResult) {
        thresholdAlertRepositoryHandler.deactivateAlert(notificationResult.getAlertId());
        log.info("Deactivated alert for user: {}", notificationResult.getEmail());

        PriceAlertEmailData priceAlertEmailData = createEmailData(notificationResult);
        sendEmail(priceAlertEmailData);
    }

    private PriceAlertEmailData createEmailData(NotificationResult notificationResult) {
        return PriceAlertEmailData.builder()
                .firstName(notificationResult.getFirstName())
                .lastName(notificationResult.getLastName())
                .email(notificationResult.getEmail())
                .thresholdPrice(String.valueOf(notificationResult.getThresholdPrice()))
                .alertDateTime(DateTimeUtil.getFormattedTime())
                .emailTemplate(EmailTemplate.PRICE_ALERT_TEMPLATE)
                .build();
    }

    private void sendEmail(PriceAlertEmailData priceAlertEmailData) {
        String jsonEmailData = JsonUtil.convertObjectToJson(priceAlertEmailData);
        kafkaService.sendMessage(KafkaTopic.SEND_PRICE_ALERT, jsonEmailData);
    }
}
