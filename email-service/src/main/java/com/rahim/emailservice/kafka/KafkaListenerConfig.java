package com.rahim.emailservice.kafka;

import com.rahim.emailservice.service.IKafkaDataProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * This is a configuration class that listens to Kafka topics.
 * It consumes the data provided from the topics and redirects it to the appropriate method for processing.
 *
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IKafkaDataProcessor kafkaDataProcessor;

    /**
     * This method is a Kafka listener that listens to the specified topic and processes incoming email data.
     * <p>
     * When a message arrives at the "${topics.send-email-topic}" topic, this method is automatically invoked by Spring Kafka.
     * The message payload (email data) is passed to this method as a parameter.
     * <p>
     * This method logs the receipt of the email data and then delegates the processing of the data to the {@code kafkaDataProcessor}.
     *
     * @param emailData the email data received from Kafka. This data is expected to be in a format that {@code kafkaDataProcessor} can process.
     */
    @KafkaListener(topics = "${topics.send-email-topic}", groupId = "group2")
    public void sendEmail(String emailData) {
        LOG.debug("Email data received. Attempting to process data and send email...");
        kafkaDataProcessor.processKafkaData(emailData);
    }

}
