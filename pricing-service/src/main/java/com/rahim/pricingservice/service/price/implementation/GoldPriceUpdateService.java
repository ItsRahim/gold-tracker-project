package com.rahim.pricingservice.service.price.implementation;

import com.rahim.pricingservice.constant.TopicConstants;
import com.rahim.pricingservice.kafka.IKafkaService;
import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.model.GoldPrice;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldPriceRepository;
import com.rahim.pricingservice.service.price.IGoldPriceUpdateService;
import com.rahim.pricingservice.service.repository.IGoldPriceRepositoryHandler;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@RequiredArgsConstructor
public class GoldPriceUpdateService implements IGoldPriceUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceUpdateService.class);

    private final IGoldPriceRepositoryHandler goldPriceRepository;
    private final IGoldTypeRepositoryHandler goldTypeRepository;
    private final GoldPriceCalculator goldPriceCalculator;
    private final TopicConstants topicConstants;
    private final IKafkaService kafkaService;

    private static final String GOLD_TICKER = "XAUGBP";
    private static final int GOLD_TICKER_ID = 1;

    @Override
    public void updateGoldTickerPrice(GoldData processedData) {
        try {
            validateProcessedData(processedData);

            Optional<GoldPrice> goldPriceOptional = goldPriceRepository.findById(GOLD_TICKER_ID);

            goldPriceOptional.ifPresent(goldTicker -> {
                BigDecimal newPrice = processedData.getPrice().setScale(2, RoundingMode.HALF_UP);
                updateGoldPrice(goldTicker, newPrice);
                kafkaService.sendMessage(topicConstants.getSendNotificationPriceTopic(), newPrice.toString());

                LOG.info("Gold ticker price updated successfully. New price: {}, Updated time: {}", newPrice, goldTicker.getUpdatedAt());

                updateGoldPrices();
            });

            if (goldPriceOptional.isEmpty()) {
                LOG.warn("Gold ticker not found in the repository. Unable to update.");
            }
        } catch (Exception e) {
            LOG.error("Error updating gold ticker price: {}", e.getMessage(), e);
        }
    }

    private void validateProcessedData(GoldData processedData) {
        if (processedData == null) {
            LOG.error("API data is null. Unable to update gold ticker price");
            throw new IllegalArgumentException("API data is null. Unable to update gold ticker price.");
        }
    }

    private void updateGoldPrice(GoldPrice goldPrice, BigDecimal newPrice) {
        goldPrice.setCurrentPrice(newPrice);
        goldPrice.setUpdatedAt(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        goldPriceRepository.saveGoldPrice(goldPrice);
    }

    private void updateGoldPrices() {
        try {
            List<Integer> goldTypeIds = goldTypeRepository.allGoldTypeIds();
            int numOfUpdates = goldTypeIds.size();

            for (int goldTypeId : goldTypeIds) {
                updateGoldPricesForType(goldTypeId);
            }

            LOG.info("Updated gold prices for {} Gold Types", numOfUpdates);
        } catch (Exception e) {
            LOG.error("Error updating gold prices: {}", e.getMessage(), e);
        }
    }

    private void updateGoldPricesForType(int goldTypeId) {
        List<GoldPrice> pricesToUpdate = goldPriceRepository.findByTypeId(goldTypeId);
        pricesToUpdate.forEach(this::updateGoldPrice);
    }

    private void updateGoldPrice(GoldPrice goldPrice) {
        GoldType goldType = goldPrice.getGoldType();
        if (goldType.getName().equals(GOLD_TICKER)) {
            LOG.info("Skipping update for gold type with name XAUGBP. Gold Type ID: {}", goldType.getId());
            return;
        }

        BigDecimal newPrice = calculateNewGoldPrice(goldType);
        goldPrice.setCurrentPrice(newPrice);
        goldPrice.setUpdatedAt(OffsetDateTime.now());
        goldPriceRepository.save(goldPrice);
    }

    private BigDecimal calculateNewGoldPrice(GoldType goldType) {
        BigDecimal netWeight = goldType.getNetWeight();
        String carat = goldType.getCarat();
        return goldPriceCalculator.calculateGoldPrice(carat, netWeight);
    }
}

