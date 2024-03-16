package com.rahim.pricingservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.service.price.IGoldPriceUpdateService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 04/12/2023
 */
@Getter
@Component
@RequiredArgsConstructor
public class ApiDataProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ApiDataProcessor.class);

    private final GoldPriceCalculator goldPriceCalculator;
    private final IGoldPriceUpdateService goldPriceUpdateService;
    private GoldData processedData;

    public void processApiData(String kafkaData) {
        try {
            processedData = new GoldData(kafkaData);
            goldPriceCalculator.calculatePricePerGram(processedData.getPrice());
            goldPriceUpdateService.updateGoldTickerPrice(processedData);

        } catch (JsonProcessingException e) {
            LOG.error("Error processing API data: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing API data", e);
        } catch (Exception e) {
            LOG.error("Unexpected error processing API data: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error processing API data", e);
        }
    }
}
