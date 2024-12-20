package com.rahim.pricingservice.service.history.implementation;

import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.entity.GoldPriceHistory;
import com.rahim.pricingservice.repository.GoldPriceHistoryRepository;
import com.rahim.pricingservice.service.history.IGoldPriceHistoryService;
import com.rahim.pricingservice.util.ApiDataProcessor;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GoldPriceHistoryService implements IGoldPriceHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceHistoryService.class);
    private final GoldPriceHistoryRepository goldPriceHistoryRepository;
    private final ApiDataProcessor apiDataProcessor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHistoryTable() {
        try {
            GoldData apiData = apiDataProcessor.getProcessedData();
            BigDecimal pricePerOunce = apiData.getPrice();
            LocalDate effectiveDate = LocalDate.now();
            BigDecimal pricePerGram = GoldPriceCalculator.getPricePerGram();

            GoldPriceHistory priceHistory = new GoldPriceHistory(pricePerOunce, pricePerGram, effectiveDate);
            goldPriceHistoryRepository.save(priceHistory);

            LOG.debug("Gold price history updated successfully");
        } catch (Exception e) {
            LOG.error("Error updating gold price history", e);
            throw new RuntimeException("Error updating gold price history", e);
        }
    }

}
