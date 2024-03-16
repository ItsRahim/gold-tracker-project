package com.rahim.pricingservice.service.history.implementation;

import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.model.GoldPriceHistory;
import com.rahim.pricingservice.repository.GoldPriceHistoryRepository;
import com.rahim.pricingservice.service.history.IGoldPriceHistoryService;
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
    private final ApiDataProcessor apiDataProcessor;

    @Override
    public void updateHistoryTable() {
        try {
            GoldData apiData = apiDataProcessor.getProcessedData();
            BigDecimal pricePerOunce = apiData.getPrice();
            LocalDate effectiveDate = LocalDate.now();
            BigDecimal pricePerGram = GoldPriceCalculator.getPricePerGram();

            GoldPriceHistory priceHistory = new GoldPriceHistory(pricePerOunce, pricePerGram, effectiveDate);
            goldPriceHistoryRepository.save(priceHistory);

            LOG.info("Gold price history updated successfully");
        } catch (Exception e) {
            LOG.error("Error updating gold price history", e);
            throw new RuntimeException("Error updating gold price history", e);
        }
    }

}
