package com.rahim.pricingservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.pricingservice.model.GoldData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class ApiDataProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(ApiDataProcessor.class);
    private final GoldPriceCalculator goldPriceCalculator;
    private GoldData apiData;

    public void setKafkaData(String data) {
        apiData = processApiData(data);
    }

    public GoldData processApiData(String kafkaData) {
        try {
            GoldData apiData = new GoldData(kafkaData);
            goldPriceCalculator.setPricePerOunce(apiData.getPrice());
            goldPriceCalculator.calculatePricePerGram();

            return apiData;
        } catch (JsonProcessingException e) {
            LOG.error("Error processing API data: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("Unexpected error processing API data: {}", e.getMessage(), e);
        }
        return null;
    }
}

