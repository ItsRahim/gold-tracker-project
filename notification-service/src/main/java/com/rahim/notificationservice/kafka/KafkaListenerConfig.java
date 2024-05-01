package com.rahim.notificationservice.kafka;

import com.rahim.notificationservice.service.kafka.IKafkaDataProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Getter
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IKafkaDataProcessor kafkaDataProcessor;

    @KafkaListener(topics = "${topics.send-notification-price}", groupId = "group2")
    public void priceListener(String priceData) {
        LOG.info("Message received from Pricing Service: {}", priceData);
        kafkaDataProcessor.processKafkaData(priceData);
    }

}
