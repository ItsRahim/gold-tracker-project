package com.rahim.userservice.kafka;

import com.rahim.userservice.service.IInternalUserService;
import com.rahim.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IInternalUserService internalUserService;
    private final IUserService userService;

    @KafkaListener(topics = "${topics.scheduler-user-cleanup}", groupId = "group2")
    public void cleanupUserAccounts(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        internalUserService.runCleanupJob();
    }

    @KafkaListener(topics = "${topics.check-user-id}", groupId = "group2")
    public void checkForUserId(String userId) {
        LOG.info("Message received from Notification Service: {}", userId);
        userService.existsById(userId);
    }
}

