package com.rahim.pricingservice.kafka;

import com.rahim.pricingservice.service.IGoldPriceService;
import com.rahim.pricingservice.service.feign.IGoldPriceFeignClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IGoldPriceFeignClient goldPriceFeignClient;
    private final IGoldPriceService goldPriceService;

    @KafkaListener(topics = "pricing-service-update-prices", groupId = "group2")
    public void updateGoldPriceJob(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        goldPriceFeignClient.getGoldPrice();
    }

    @KafkaListener(topics = "gold-price-stream", groupId = "group2")
    public void processPriceChange(String priceData) {
        if (priceData.isEmpty()) {
            LOG.error("No gold price data received from the custom pricing API.");
        } else {
            LOG.info("Successfully received the latest gold prices. Processing data...");
            goldPriceService.setKafkaData(priceData);
        }
    }

    @KafkaListener(topics = "pricing-service-new-type", groupId = "group2")
    public void addNewGoldPrice(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        goldPriceService.processNewGoldType(Integer.parseInt(message));
    }

    @KafkaListener(topics = "pricing-service-delete-type", groupId = "group2")
    public void removeGoldPrice(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        goldPriceService.deleteGoldPrice(Integer.parseInt(message));
    }
}
