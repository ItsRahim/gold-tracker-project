package com.rahim.notificationservice.kafka;

import com.hazelcast.collection.ISet;
import com.rahim.notificationservice.service.hazelcast.CacheManager;
import com.rahim.notificationservice.service.kafka.IKafkaDataProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IKafkaDataProcessor kafkaDataProcessor;
    private final CacheManager hazelcastCacheManager;

    @Value("${hazelcast.sets.kafka}")
    String kafkaSet;

    @KafkaListener(topics = "${topics.send-notification-price}", groupId = "group2")
    public void priceListener(String priceData) {
        int index = priceData.indexOf('_');
        String processedData = index != -1 ? priceData.substring(0, index) : priceData;
        if (!isProcessed(processedData)) {
            kafkaDataProcessor.processKafkaData(processedData);
            markAsProcessed(processedData);
        } else {
            LOG.debug("Message '{}' has already been processed. Skipping sending email notification event", processedData);
        }
    }

    private boolean isProcessed(String message) {
        ISet<String> processedMessages = hazelcastCacheManager.getSet(kafkaSet);
        if (processedMessages != null) {
            return processedMessages.contains(message);
        } else {
            LOG.warn("Failed to check if message '{}' is processed. Hazelcast set '{}' is not available.", message, kafkaSet);
            return false;
        }
    }

    private void markAsProcessed(String message) {
        ISet<String> processedMessages = hazelcastCacheManager.getSet(kafkaSet);
        if (processedMessages != null) {
            hazelcastCacheManager.addToSet(kafkaSet, message);
            LOG.debug("Message marked as processed: '{}'", message);
        } else {
            LOG.warn("Failed to mark message as processed. Hazelcast set '{}' is not available.", kafkaSet);
        }
    }

}
