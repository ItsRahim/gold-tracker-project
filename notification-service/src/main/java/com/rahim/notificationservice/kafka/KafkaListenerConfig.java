package com.rahim.notificationservice.kafka;

import com.rahim.notificationservice.service.IKafkaDataProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CompletableFuture;

@Getter
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IKafkaDataProcessor kafkaDataProcessor;
    private final CompletableFuture<String> responseFuture = new CompletableFuture<>();

    @KafkaListener(topics = "${topics.send-notification-price}", groupId = "group2")
    public void priceListener(String priceData) {
        LOG.info("Message received from Pricing Service: {}", priceData);
        kafkaDataProcessor.processKafkaData(priceData);
    }

    @KafkaListener(topics = "${topics.send-id-result}", groupId = "group2")
    public void idResultListener(String idResultData) {
        LOG.info("Message received from ID Result Service: {}", idResultData);
        responseFuture.complete(idResultData);
    }
}
