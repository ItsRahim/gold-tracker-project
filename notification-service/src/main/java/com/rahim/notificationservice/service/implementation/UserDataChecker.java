package com.rahim.notificationservice.service.implementation;

import com.rahim.notificationservice.kafka.KafkaTopic;
import com.rahim.notificationservice.kafka.IKafkaService;
import com.rahim.notificationservice.kafka.KafkaListenerConfig;
import com.rahim.notificationservice.service.IUserDataChecker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserDataChecker implements IUserDataChecker {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataChecker.class);
    private final KafkaListenerConfig kafkaListenerConfig;
    private final IKafkaService kafkaService;
    private final KafkaTopic kafkaTopic;

    @Override
    public Boolean isNotificationValid(String userId) {
        CompletableFuture<Boolean> resultFuture = new CompletableFuture<>();
        try {
            checkId(userId)
                    .thenAccept(resultFuture::complete)
                    .exceptionally(ex -> {
                        LOG.error("Error checking user existence", ex);
                        resultFuture.complete(false);
                        return null;
                    });

            return resultFuture.get();
        } catch (Exception e) {
            LOG.error("Error initiating user existence check", e);
            return false;
        }
    }

    private CompletableFuture<Boolean> checkId(String userId) {
        CompletableFuture<Boolean> resultFuture = new CompletableFuture<>();
        kafkaService.sendMessage(kafkaTopic.getCheckIdExists(), userId);

        kafkaListenerConfig.getResponseFuture().whenComplete((response, throwable) -> {
            if (throwable != null) {
                resultFuture.completeExceptionally(throwable);
            } else {
                boolean idExists = Boolean.parseBoolean(response);
                resultFuture.complete(idExists);
            }
        });

        return resultFuture;
    }
}
