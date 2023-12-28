package com.rahim.notificationservice.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.notificationservice.constants.TopicConstants;
import com.rahim.notificationservice.enums.TemplateNameEnum;
import com.rahim.notificationservice.kafka.IKafkaService;
import com.rahim.notificationservice.repository.ThresholdAlertRepository;
import com.rahim.notificationservice.service.IKafkaDataProcessor;
import com.rahim.notificationservice.service.IThresholdAlertRepositoryHandler;
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

@Service
@RequiredArgsConstructor
public class KafkaDataProcessor implements IKafkaDataProcessor {
    private final Logger LOG = LoggerFactory.getLogger(KafkaDataProcessor.class);
    private final IMessageFormatter messageFormatter;
    private final ThresholdAlertRepository thresholdAlertRepository;
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;

    @Override
    public void processKafkaData(String priceData) {
        try {
            BigDecimal currentPrice = new BigDecimal(priceData);

            List<Map<String, Object>> notificationList = thresholdAlertRepository.getEmailTokens(currentPrice);

            for (Map<String, Object> notificationMap : notificationList) {
                boolean isActive = (boolean) notificationMap.get("is_active");
                int alertId = (int) notificationMap.get("alert_id");

                if(!isActive) {
                    continue;
                }
                LOG.info("Found user to alert");

                Map<String, Object> mutableEmailData = new HashMap<>(notificationMap);

                thresholdAlertRepositoryHandler.deactivateAlert(alertId);
                updateKeys(mutableEmailData);

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
    }

    private void updateKeys(Map<String, Object> data) {
        String alertDateTime = getFormattedTime();

        messageFormatter.updateMapKey(data, "first_name", "firstName");
        messageFormatter.updateMapKey(data, "last_name", "lastName");
        messageFormatter.updateMapKey(data, "threshold_price", "thresholdPrice");

        data.remove("alert_id");
        data.remove("is_active");

        data.put("alertDateTime", alertDateTime);
        data.put("templateName", TemplateNameEnum.PRICE_ALERT.getTemplateName());
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
}
