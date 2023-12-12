package com.rahim.emailservice.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);

    @KafkaListener(topics = "email-service-inactive-account", groupId = "group2")
    public void sendInactiveEmail(String emailData) {
        LOG.info("Message received from User Microservice");
    }
}
