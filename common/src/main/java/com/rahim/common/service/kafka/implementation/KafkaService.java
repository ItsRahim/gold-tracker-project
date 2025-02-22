package com.rahim.common.service.kafka.implementation;

import com.rahim.common.config.health.HealthCheck;
import com.rahim.common.service.kafka.IKafkaService;
import com.rahim.common.service.kafka.KafkaFailover;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Rahim Ahmed
 * @created 18/11/2023
 */
@Service
@RequiredArgsConstructor
public class KafkaService implements IKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaFailover kafkaFailover;

    @Override
    @HealthCheck(type = "kafka")
    public void sendMessage(String topic, String data) {
        try {
            Message<String> message = generateMessage(topic, data);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    LOG.error("Error sending message to topic '{}': {}", topic, ex.getMessage(), ex);
                    kafkaFailover.persistToDb(topic, data);
                } else {
                    LOG.trace("Message sent to topic '{}' successfully. Partition: {}, Offset: {}",
                            topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }
            });

            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error occurred in sending message: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private Message<String> generateMessage(String topic, String data) {
        String key = UUID.randomUUID().toString();

        return MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, key)
                .setHeader(KafkaHeaders.TIMESTAMP, System.currentTimeMillis())
                .build();
    }

}
