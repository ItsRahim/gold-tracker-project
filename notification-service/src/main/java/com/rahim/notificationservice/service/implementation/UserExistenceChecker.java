package com.rahim.notificationservice.service.implementation;

import com.rahim.notificationservice.constants.TopicConstants;
import com.rahim.notificationservice.kafka.IKafkaService;
import com.rahim.notificationservice.kafka.KafkaListenerConfig;
import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.IUserExistenceChecker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserExistenceChecker implements IUserExistenceChecker {
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;
    private final KafkaListenerConfig kafkaListenerConfig;
    private static final Logger LOG = LoggerFactory.getLogger(UserExistenceChecker.class);

    @Override
    public boolean checkUserExistence(String userId, ThresholdAlert thresholdAlert) {
        CompletableFuture<Boolean> idExistsFuture = checkId(userId);
        try {
            return idExistsFuture.get();
        } catch (Exception e) {
            LOG.error("Error checking user existence", e);
            return false;
        }
    }

    private CompletableFuture<Boolean> checkId(String userId) {
        CompletableFuture<Boolean> resultFuture = new CompletableFuture<>();

        kafkaService.sendMessage(topicConstants.getCheckIdExists(), userId);

        CompletableFuture<String> responseFuture = kafkaListenerConfig.getResponseFuture();

        responseFuture.whenComplete((response, throwable) -> {
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

