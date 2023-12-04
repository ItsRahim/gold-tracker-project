package com.rahim.pricingservice.service.implementation;

import com.rahim.pricingservice.model.GoldPriceHistory;
import com.rahim.pricingservice.repository.GoldPriceHistoryRepository;
import com.rahim.pricingservice.service.IGoldPriceHistoryService;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GoldPriceHistoryServiceImplementation implements IGoldPriceHistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceHistoryServiceImplementation.class);
    private final GoldPriceHistoryRepository goldPriceHistoryRepository;
    private final GoldPriceCalculator goldPriceCalculator;

    @Override
    public void updateHistoryTable(BigDecimal pricePerOunce) {
        try {
            goldPriceCalculator.calculatePricePerGram(pricePerOunce);
            LocalDate effectiveDate = LocalDate.now();
            BigDecimal pricePerGram = BigDecimal.valueOf(goldPriceCalculator.getPricePerGram());

            GoldPriceHistory priceHistory = new GoldPriceHistory(pricePerOunce, pricePerGram, effectiveDate);
            goldPriceHistoryRepository.save(priceHistory);

            LOG.info("Gold price history updated successfully. Price per Ounce: {}, Price per Gram: {}, Effective Date: {}",
                    pricePerOunce, pricePerGram, effectiveDate);
        } catch (Exception e) {
            LOG.error("An error occurred while updating gold price history. Price per Ounce: {}", pricePerOunce, e);
            throw new RuntimeException(e);
        }
    }
}
