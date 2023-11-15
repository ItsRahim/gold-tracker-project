package com.rahim.schedulerservice.kafka.implementation;

import com.rahim.schedulerservice.kafka.IKafkaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class KafkaService implements IKafkaService {
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void sendMessage(String topic, String data) {
        log.info("Sending Message to Kafka...");
        kafkaTemplate.send(topic, data);
    }
}
