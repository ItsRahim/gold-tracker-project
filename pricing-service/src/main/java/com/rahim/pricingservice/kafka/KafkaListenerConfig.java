package com.rahim.pricingservice.kafka;

import com.rahim.pricingservice.service.IGoldPriceHistoryService;
import com.rahim.pricingservice.service.IGoldPriceService;
import com.rahim.pricingservice.service.feign.IGoldPriceFeignClient;
import com.rahim.pricingservice.util.ApiDataProcessor;
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
    private final IGoldPriceHistoryService goldPriceHistoryService;
    private final ApiDataProcessor apiDataProcessor;

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
            apiDataProcessor.setKafkaData(priceData);
            goldPriceService.updateGoldTickerPrice();
        }
    }

    @KafkaListener(topics = "pricing-service-new-type", groupId = "group2")
    public void addNewGoldPrice(String goldTypeId) {
        LOG.info("Message received. New Gold Type added: {}", goldTypeId);
        goldPriceService.processNewGoldType(Integer.parseInt(goldTypeId));
    }

    @KafkaListener(topics = "pricing-service-delete-type", groupId = "group2")
    public void removeGoldPrice(String goldTypeId) {
        LOG.info("Message received to remove gold type with ID: {}", goldTypeId);
        goldPriceService.deleteGoldPrice(Integer.parseInt(goldTypeId));
    }

    @KafkaListener(topics = "pricing-service-history-table", groupId = "group2")
    public void updateHistoryTable(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        goldPriceFeignClient.getGoldPrice();
        goldPriceHistoryService.updateHistoryTable();
    }
}

