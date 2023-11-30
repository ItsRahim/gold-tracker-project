package com.rahim.userservice.kafka;

import com.rahim.userservice.service.IInternalUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IInternalUserService internalUserService;

    @KafkaListener(topics = "user-service-cleanup-accounts", groupId = "group2")
    public void cleanupUserAccounts(String message) {
        log.info("Message received from Scheduler Service: {}", message);
        internalUserService.findAllInactiveUsers();
        internalUserService.processInactiveUsers();
        internalUserService.processPendingDeleteUsers();
    }
}

