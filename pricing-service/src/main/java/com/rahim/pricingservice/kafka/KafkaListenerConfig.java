package com.rahim.pricingservice.kafka;

import com.rahim.pricingservice.service.history.IGoldPriceHistoryService;
import com.rahim.pricingservice.feign.IGoldPriceFeignClient;
import com.rahim.pricingservice.util.ApiDataProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author Rahim Ahmed
 * @created 01/12/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private final IGoldPriceFeignClient goldPriceFeignClient;
    private final IGoldPriceHistoryService goldPriceHistoryService;
    private final ApiDataProcessor apiDataProcessor;

    @KafkaListener(topics = "${topics.update-gold-price-job}", groupId = "group2")
    public void updateGoldPriceJob() {
        goldPriceFeignClient.getGoldPrice();
    }

    @KafkaListener(topics = "${topics.custom-api-data}", groupId = "group2")
    public void processPriceChange(String priceData) {
        apiDataProcessor.processApiData(priceData);
    }

    @KafkaListener(topics = "${topics.update-price-history}", groupId = "group2")
    public void updateHistoryTable() {
        goldPriceFeignClient.getGoldPrice();
        goldPriceHistoryService.updateHistoryTable();
    }
}
