package com.rahim.accountservice.config.kafka;

import com.rahim.accountservice.service.account.IInternalAccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author Rahim Ahmed
 * @created 18/11/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IInternalAccountService internalUserService;

    @KafkaListener(topics = "${topics.scheduler-user-cleanup}", groupId = "group2")
    public void cleanupUserAccounts(String message) {
        LOG.debug("Message received from Scheduler Service: {}", message);
        internalUserService.runCleanupJob();
    }

}