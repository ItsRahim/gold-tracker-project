package com.rahim.common.service.kafka;

import com.hazelcast.collection.ISet;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 04/05/2024
 */
@Service
@RequiredArgsConstructor
public class MessageManager {

    private static final Logger LOG = LoggerFactory.getLogger(MessageManager.class);
    private final CacheManager hazelcastCacheManager;

    public boolean isProcessed(String message) {
        ISet<String> processedMessages = hazelcastCacheManager.getSet(HazelcastConstant.PROCESSED_KAFKA_MESSAGES);
        if (processedMessages == null) {
            LOG.warn("Failed to check if message '{}' is processed. Hazelcast set '{}' is not available.", message, HazelcastConstant.PROCESSED_KAFKA_MESSAGES);
            return false;
        }

        return processedMessages.contains(message);
    }

    public void markAsProcessed(String message) {
        ISet<String> processedMessages = hazelcastCacheManager.getSet(HazelcastConstant.PROCESSED_KAFKA_MESSAGES);
        if (processedMessages == null) {
            LOG.warn("Failed to mark message as processed. Hazelcast set '{}' is not available.", HazelcastConstant.PROCESSED_KAFKA_MESSAGES);
            return;
        }

        hazelcastCacheManager.addToSet(HazelcastConstant.PROCESSED_KAFKA_MESSAGES, message);
        LOG.debug("Message marked as processed: '{}'", message);
    }
}
