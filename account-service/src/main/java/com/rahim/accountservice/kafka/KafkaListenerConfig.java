package com.rahim.accountservice.kafka;

import com.rahim.accountservice.service.account.IAccountQueryService;
import com.rahim.accountservice.service.account.IInternalAccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IInternalAccountService internalUserService;
    private final IAccountQueryService accountQueryService;

    @KafkaListener(topics = "${topics.scheduler-user-cleanup}", groupId = "group2")
    public void cleanupUserAccounts(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        internalUserService.runCleanupJob();
    }

    @KafkaListener(topics = "${topics.check-user-id}", groupId = "group2")
    public void checkForUserId(String userId) {
        LOG.info("Message received from Notification Service: {}", userId);
        accountQueryService.existsById(userId);
    }
}