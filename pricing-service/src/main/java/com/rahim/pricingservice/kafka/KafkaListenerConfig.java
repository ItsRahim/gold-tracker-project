package com.rahim.pricingservice.kafka;

import com.rahim.pricingservice.service.IGoldPriceHistoryService;
import com.rahim.pricingservice.service.feign.IGoldPriceFeignClient;
import com.rahim.pricingservice.util.ApiDataProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author Rahim Ahmed
 * @created 01/12/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);

    private final IGoldPriceFeignClient goldPriceFeignClient;
    private final IGoldPriceHistoryService goldPriceHistoryService;
    private final ApiDataProcessor apiDataProcessor;

    @KafkaListener(topics = "${topics.update-gold-price-job}", groupId = "group2")
    public void updateGoldPriceJob(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        goldPriceFeignClient.getGoldPrice();
    }

    @KafkaListener(topics = "${topics.custom-api-data}", groupId = "group2")
    public void processPriceChange(String priceData) {
        apiDataProcessor.processApiData(priceData);
    }

    @KafkaListener(topics = "${topics.update-price-history}", groupId = "group2")
    public void updateHistoryTable(String message) {
        LOG.info("Message received from Scheduler Service: {}", message);
        goldPriceFeignClient.getGoldPrice();
        goldPriceHistoryService.updateHistoryTable();
    }
}
