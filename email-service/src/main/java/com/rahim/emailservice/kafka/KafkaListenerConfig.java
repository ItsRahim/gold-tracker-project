package com.rahim.emailservice.kafka;

import com.rahim.emailservice.service.IKafkaDataProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IKafkaDataProcessor kafkaDataProcessor;

    @KafkaListener(topics = "${topics.send-email-topic}", groupId = "group2")
    public void sendEmail(String emailData) {
        LOG.debug("Email data received. Attempting to process data and send email...");
        kafkaDataProcessor.processKafkaData(emailData);
    }

}
