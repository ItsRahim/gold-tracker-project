package com.rahim.pricingservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaListenerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);

    @KafkaListener(topics = "pricing-service-update-prices", groupId = "group2")
    public void updateGoldPriceJob(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
    }
}