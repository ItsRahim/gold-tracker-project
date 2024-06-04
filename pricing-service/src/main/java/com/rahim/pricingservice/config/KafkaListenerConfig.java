package com.rahim.pricingservice.config;

import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.MessageManager;
import com.rahim.common.util.KafkaUtil;
import com.rahim.pricingservice.service.history.IGoldPriceHistoryService;
import com.rahim.pricingservice.api.GoldPriceApiClient;
import com.rahim.pricingservice.util.ApiDataProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * @author Rahim Ahmed
 * @created 01/12/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IGoldPriceHistoryService goldPriceHistoryService;
    private final GoldPriceApiClient goldPriceFeignClient;
    private final ApiDataProcessor apiDataProcessor;
    private final MessageManager messageManager;

    @KafkaListener(topics = KafkaTopic.PRICE_UPDATE, groupId = "group2")
    void updateGoldPriceJob(String message) {
        if (messageManager.isProcessed(message)) {
            LOG.debug("Message '{}' has already been processed. Skipping update price job.", message);
            return;
        }

        goldPriceFeignClient.getGoldPrice();
        messageManager.markAsProcessed(message);
    }

    @KafkaListener(topics = "${python-api.topic}", groupId = "group2")
    void processPriceChange(@Payload String priceData) {
        if (messageManager.isProcessed(priceData)) {
            LOG.debug("Price data '{}' has already been processed", priceData);
            return;
        }

        String data = KafkaUtil.extractPriceData(priceData);

        if (data == null) {
            LOG.error("Data format incorrect/null. Unable to process price data");
            return;
        }

        apiDataProcessor.processApiData(data);
        messageManager.markAsProcessed(priceData);
    }

    @KafkaListener(topics = KafkaTopic.PRICE_HISTORY_UPDATE, groupId = "group2")
    void updateHistoryTable(String message) {
        if (messageManager.isProcessed(message)) {
            LOG.debug("Message '{}' has already been processed. Skipping update price history job.", message);
            return;
        }

        goldPriceFeignClient.getGoldPrice();
        goldPriceHistoryService.updateHistoryTable();
        messageManager.markAsProcessed(message);
    }
}
