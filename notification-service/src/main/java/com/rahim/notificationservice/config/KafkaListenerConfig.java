package com.rahim.notificationservice.config;

import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.MessageManager;
import com.rahim.common.util.KafkaUtil;
import com.rahim.notificationservice.service.kafka.IKafkaDataProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IKafkaDataProcessor kafkaDataProcessor;
    private final MessageManager messageManager;

    @KafkaListener(topics = KafkaTopic.THRESHOLD_PRICE_UPDATE, groupId = "group2")
    void priceListener(String priceData) {
        if (messageManager.isProcessed(priceData)) {
            LOG.debug("Message '{}' has already been processed. Skipping sending email notification event", priceData);
            return;
        }

        String kafkaData = KafkaUtil.extractDataFromKey(priceData);
        kafkaDataProcessor.processKafkaData(kafkaData);
        messageManager.markAsProcessed(priceData);
    }
}
