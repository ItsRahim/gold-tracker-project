package com.rahim.notificationservice.kafka.implementation;

import com.rahim.notificationservice.kafka.IKafkaService;
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

    private static final Logger LOG = LoggerFactory.getLogger(KafkaService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String data) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, data);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    LOG.error("Error sending message to topic '{}': {}", topic, ex.getMessage(), ex);
                } else {
                    LOG.debug("Message sent to topic '{}' successfully. Partition: {}, Offset: {}",
                            topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }
            });

            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error occurred in sending message: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            kafkaTemplate.destroy();
        }
    }
}
