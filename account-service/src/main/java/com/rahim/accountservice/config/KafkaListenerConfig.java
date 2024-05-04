package com.rahim.accountservice.config;

import com.rahim.accountservice.service.account.IInternalAccountService;
import com.rahim.common.service.kafka.MessageManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final MessageManager messageManager;

    @KafkaListener(topics = "${topics.scheduler-user-cleanup}", groupId = "group2")
    public void cleanupUserAccounts(String message) {
        if (!messageManager.isProcessed(message)) {
            internalUserService.runCleanupJob();
            messageManager.markAsProcessed(message);
        } else {
            LOG.debug("Message '{}' has already been processed. Skipping cleanup job.", message);
        }
    }

}