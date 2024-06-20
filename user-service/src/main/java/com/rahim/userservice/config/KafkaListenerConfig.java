package com.rahim.userservice.config;

import com.rahim.userservice.service.account.IInternalAccountService;
import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.MessageManager;
import com.rahim.common.util.KafkaUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 18/11/2023
 */
@Component
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IInternalAccountService internalUserService;
    private final MessageManager messageManager;

    @KafkaListener(topics = KafkaTopic.ACCOUNT_CLEANUP, groupId = "group2")
    void cleanupUserAccounts(@Payload String message,
                             @Header(KafkaHeaders.RECEIVED_KEY) String key,
                             @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                             ConsumerRecord<String, String> consumerRecord,
                             Acknowledgment acknowledgment) {

        KafkaUtil.logReceivedMessage(message, key, consumerRecord, ts);

        if (messageManager.isProcessed(key)) {
            log.debug("Message '{}' has already been processed. Skipping cleanup job.", message);
            acknowledgment.acknowledge();
            return;
        }

        internalUserService.runCleanupJob();
        messageManager.markAsProcessed(key);
        acknowledgment.acknowledge();
    }

}