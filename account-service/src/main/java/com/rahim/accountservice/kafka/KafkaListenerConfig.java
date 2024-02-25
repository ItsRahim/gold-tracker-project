package com.rahim.accountservice.kafka;

import com.rahim.accountservice.service.account.IAccountQueryService;
import com.rahim.accountservice.service.account.IInternalAccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * This is a configuration class that listens to Kafka topics.
 * It consumes the data provided from the topics and redirects it to the appropriate method for processing.
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IInternalAccountService internalUserService;
    private final IAccountQueryService accountQueryService;

    /**
     * This method listens to messages provided by the scheduler-service and initiates the purging of user accounts.
     * It is triggered by a Kafka message on the topic specified in the application's configuration under 'topics.scheduler-user-cleanup'.
     *
     * @param message - The incoming message from the scheduler-service. Although it's not used in the method, it's required for the method signature as per the KafkaListener annotation.
     *
     * Note: The actual cleanup job is performed by the 'runCleanupJob' method of the 'internalUserService'.
     */
    @KafkaListener(topics = "${topics.scheduler-user-cleanup}", groupId = "group2")
    public void cleanupUserAccounts(String message) {
        LOG.debug("Message received from Scheduler Service: {}", message);
        internalUserService.runCleanupJob();
    }

    /**
     * This method listens to messages provided by the notification-service.
     * It is used by the notification-service to verify if a user with a given ID exists in the database.
     *
     * @param userId - The ID of the user to be checked in the database.
     *
     * Note: The actual existence check is performed by the 'existsById' method of the 'accountQueryService'.
     */
    @KafkaListener(topics = "${topics.check-user-id}", groupId = "group2")
    public void checkForUserId(String userId) {
        LOG.debug("Message received from Notification Service: {}", userId);
        accountQueryService.checkNotificationCriteria(userId);
    }
}