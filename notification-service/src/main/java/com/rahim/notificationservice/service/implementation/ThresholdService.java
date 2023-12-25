package com.rahim.notificationservice.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.notificationservice.constants.TopicConstants;
import com.rahim.notificationservice.enums.TemplateNameEnum;
import com.rahim.notificationservice.kafka.IKafkaService;
import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.repository.ThresholdAlertRepository;
import com.rahim.notificationservice.service.IThresholdService;
import com.rahim.notificationservice.util.IMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThresholdService implements IThresholdService {
    private static final Logger LOG = LoggerFactory.getLogger(ThresholdService.class);
    private final ThresholdAlertRepository thresholdAlertRepository;
    private final IMessageFormatter messageFormatter;
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;

    @Override
    public List<String> processKafkaData(String priceData) {
        try {
            BigDecimal currentPrice = new BigDecimal(priceData);
            String alertDateTime = getFormattedTime();

            List<Map<String, Object>> notificationList = thresholdAlertRepository.getEmailTokens(currentPrice);

            for (Map<String, Object> notificationMap : notificationList) {
                boolean isActive = (boolean) notificationMap.get("is_active");
                int alertId = (int) notificationMap.get("alert_id");

                if(!isActive) {
                    continue;
                }
                LOG.info("Found user to alert");

                Map<String, Object> mutableEmailData = new HashMap<>(notificationMap);

                deactivateAlert(alertId);
                updateKeys(mutableEmailData);

                mutableEmailData.put("alertDateTime", alertDateTime);
                mutableEmailData.put("templateName", TemplateNameEnum.PRICE_ALERT.getTemplateName());

                LOG.info("Modified data: {}", mutableEmailData);

                String jsonEmailData = convertToJson(mutableEmailData);

                kafkaService.sendMessage(topicConstants.getSendEmailTopic(), jsonEmailData);
            }

            LOG.info("Found {} users in total", notificationList.size());
        } catch (NumberFormatException e) {
            LOG.error("Error parsing price data. Invalid format: {}", priceData);
        } catch (DataAccessException e) {
            LOG.error("Error accessing data from the database", e);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred", e);
        }
        return null;
    }

    @Override
    public Optional<ThresholdAlert> findById(int alertId) {
        try {
            Optional<ThresholdAlert> alertOptional = thresholdAlertRepository.findById(alertId);

            if (alertOptional.isPresent()) {
                LOG.info("Alert retrieved successfully. ID: {}", alertId);
            } else {
                LOG.info("Alert not found for ID: {}", alertId);
            }

            return alertOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving alert with ID: {}", alertId, e);
            return Optional.empty();
        }
    }

    private void updateKeys(Map<String, Object> data) {
        messageFormatter.updateMapKey(data, "first_name", "firstName");
        messageFormatter.updateMapKey(data, "last_name", "lastName");
        messageFormatter.updateMapKey(data, "threshold_price", "thresholdPrice");

        data.remove("alert_id");
        data.remove("is_active");
    }

    private String convertToJson(Map<String, Object> mutableEmailData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(mutableEmailData);
    }

    private String getFormattedTime() {
        return LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()
                .replace("T", " ");
    }

    private void deactivateAlert(int alertId) {
        try {
            findById(alertId).ifPresent(thresholdAlert -> {
                thresholdAlert.deactivate();
                thresholdAlertRepository.save(thresholdAlert);
            });
        } catch (Exception e) {
            LOG.error("An error occurred while deactivating alert with ID: {}", alertId, e);
        }
    }
}
