package com.rahim.pricingservice.util;

import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.service.price.IGoldPriceUpdateService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.rahim.common.util.JsonUtil.convertJsonToObject;

/**
 * @author Rahim Ahmed
 * @created 04/12/2023
 */
@Getter
@Component
@RequiredArgsConstructor
public class ApiDataProcessor {

    private static final Logger log = LoggerFactory.getLogger(ApiDataProcessor.class);
    private final IGoldPriceUpdateService goldPriceUpdateService;
    private GoldData processedData;

    @Transactional(rollbackFor = Exception.class)
    public void processApiData(String kafkaData) {
        try {
            kafkaData = kafkaData.substring(0, kafkaData.length() - 1)
                    .trim()
                    .replaceAll("\\\\", "");

            processedData = convertJsonToObject(kafkaData, GoldData.class);

            if (processedData == null || processedData.getPrice() == null){
                log.error("Price data is null. Unable to process");
                return;
            }

            GoldPriceCalculator.calculatePricePerGram(processedData.getPrice());
            goldPriceUpdateService.updateGoldTickerPrice(processedData);
        } catch (Exception e) {
            log.error("Unexpected error processing API data: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error processing API data", e);
        }
    }
}
