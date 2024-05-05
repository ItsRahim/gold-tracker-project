package com.rahim.notificationservice.service.kafka.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.IKafkaService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaDataProcessor implements IKafkaDataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaDataProcessor.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private final ThresholdAlertRepository thresholdAlertRepository;
    private final IKafkaService kafkaService;

    @Override
    public void processKafkaData(String priceData) {
        try {
            BigDecimal currentPrice = new BigDecimal(priceData);
            List<NotificationResult> notificationList = thresholdAlertRepository.getEmailTokens(currentPrice);
            LOG.debug("Retrieved {} notification records from the database for current price: {}", notificationList.size(), currentPrice);

            notificationList.forEach(notificationResult -> {
                thresholdAlertRepositoryHandler.deactivateAlert(notificationResult.getAlertId());
                LOG.info("Deactivated alert for user: {}", notificationResult.getEmail());

                EmailData emailData = EmailData.builder()
                        .firstName(notificationResult.getFirstName())
                        .lastName(notificationResult.getLastName())
                        .email(notificationResult.getEmail())
                        .thresholdPrice(String.valueOf(notificationResult.getThresholdPrice()))
                        .alertDateTime(getFormattedTime())
                        .emailTemplate(EmailTemplate.PRICE_ALERT_TEMPLATE)
                        .build();

                String jsonEmailData = null;
                try {
                    jsonEmailData = convertToJson(emailData);
                } catch (JsonProcessingException e) {
                    LOG.error("Error converting EmailData to JSON", e);
                }

                kafkaService.sendMessage(KafkaTopic.SEND_EMAIL, jsonEmailData);
            });
        } catch (NumberFormatException e) {
            LOG.error("Error parsing price data. Invalid format: {}", priceData);
        } catch (DataAccessException e) {
            LOG.error("Error accessing data from the database", e);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred", e);
        }
    }

    private String convertToJson(EmailData emailData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(emailData);
    }

    private String getFormattedTime() {
        return LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()
                .replace("T", " ");
    }
}
