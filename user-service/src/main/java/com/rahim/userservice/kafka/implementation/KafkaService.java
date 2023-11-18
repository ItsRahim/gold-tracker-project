package com.rahim.userservice.kafka.implementation;

import com.rahim.userservice.kafka.IKafkaService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class KafkaService implements IKafkaService {
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String data) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, data);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Error sending message to topic '{}': {}", topic, ex.getMessage(), ex);
                } else {
                    log.info("Message sent to topic '{}' successfully. Partition: {}, Offset: {}",
                            topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }
            });

            future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurred in sending message: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
