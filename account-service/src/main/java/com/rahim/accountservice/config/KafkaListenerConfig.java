package com.rahim.accountservice.config;

import com.rahim.accountservice.service.account.IInternalAccountService;
import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.MessageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 18/11/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private final IInternalAccountService internalUserService;
    private final MessageManager messageManager;

    @KafkaListener(topics = KafkaTopic.ACCOUNT_CLEANUP, groupId = "group2")
    void cleanupUserAccounts(String message) {
        if (messageManager.isProcessed(message)) {
            log.debug("Message '{}' has already been processed. Skipping cleanup job.", message);
            return;
        }

        internalUserService.runCleanupJob();
        messageManager.markAsProcessed(message);
    }

}