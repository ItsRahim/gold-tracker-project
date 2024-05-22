package com.rahim.pricingservice.service.price.implementation;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldPriceRepository;
import com.rahim.pricingservice.service.price.IGoldPriceCreationService;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@RequiredArgsConstructor
public class GoldPriceCreationService implements IGoldPriceCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceCreationService.class);
    private final GoldPriceRepository goldPriceRepository;
    private final GoldPriceCalculator goldPriceCalculator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processNewGoldType(GoldType goldType) {
        try {
            LOG.info("Processing new gold type: {}", goldType.getName());

            BigDecimal goldPrice = calculatePrice(goldType.getCarat(), goldType.getNetWeight());
            LOG.debug("Gold price calculated: £{} for carat {} and net weight {} grams", goldPrice, goldType.getCarat(), goldType.getNetWeight());

            goldPriceRepository.insertGoldPrice(goldType.getId(), goldPrice);
        } catch (Exception e) {
            LOG.error("Error processing new gold type with ID {}: {}", goldType.getId(), e.getMessage());
            throw new RuntimeException("Error processing new gold type", e);
        }
    }

    private BigDecimal calculatePrice(String carat, BigDecimal weight) {
        return goldPriceCalculator.calculateGoldPrice(carat, weight);
    }
}
