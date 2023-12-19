package com.rahim.notificationservice.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);

    @KafkaListener(topics = "notification-service-price-update", groupId = "group2")
    public void send(String priceData) {
        LOG.info("Message received from Pricing Service: {}", priceData);
    }
}
