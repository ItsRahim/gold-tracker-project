package com.rahim.accountservice.config.kafka;

import com.hazelcast.collection.ISet;
import com.rahim.accountservice.service.account.IInternalAccountService;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 18/11/2023
 */
@Component
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IInternalAccountService internalUserService;
    private final CacheManager hazelcastCacheManager;

    @Value("${hazelcast.sets.kafka}")
    String kafkaSet;

    @KafkaListener(topics = "${topics.scheduler-user-cleanup}", groupId = "group2")
    public void cleanupUserAccounts(String message) {
        if (!isProcessed(message)) {
            internalUserService.runCleanupJob();
            markAsProcessed(message);
        } else {
            LOG.debug("Message '{}' has already been processed. Skipping cleanup job.", message);
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