package com.rahim.pricingservice.service.implementation;

import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.model.GoldPriceHistory;
import com.rahim.pricingservice.repository.GoldPriceHistoryRepository;
import com.rahim.pricingservice.service.IGoldPriceHistoryService;
import com.rahim.pricingservice.util.ApiDataProcessor;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GoldPriceHistoryService implements IGoldPriceHistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceHistoryService.class);
    private final GoldPriceHistoryRepository goldPriceHistoryRepository;
    private final GoldPriceCalculator goldPriceCalculator;
    private final ApiDataProcessor apiDataProcessor;
    private BigDecimal pricePerOunce;

    @Override
    public void updateHistoryTable() {
        try {
            GoldData apiData = apiDataProcessor.getApiData();
            pricePerOunce = apiData.getPrice();
            LocalDate effectiveDate = LocalDate.now();
            BigDecimal pricePerGram = BigDecimal.valueOf(goldPriceCalculator.getPricePerGram());

            GoldPriceHistory priceHistory = new GoldPriceHistory(pricePerOunce, pricePerGram, effectiveDate);
            goldPriceHistoryRepository.save(priceHistory);

            LOG.info("Gold price history updated successfully");
        } catch (Exception e) {
            LOG.error("An error occurred while updating gold price history. Price per Ounce: {}", pricePerOunce, e);
            throw new RuntimeException(e);
        }
    }
}
