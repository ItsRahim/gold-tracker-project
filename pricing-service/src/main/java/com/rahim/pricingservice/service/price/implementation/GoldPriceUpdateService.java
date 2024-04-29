package com.rahim.pricingservice.service.price.implementation;

import com.rahim.pricingservice.constant.TopicConstants;
import com.rahim.pricingservice.kafka.IKafkaService;
import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.model.GoldPrice;
import com.rahim.pricingservice.model.GoldType;
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
            if (processedData == null) {
                LOG.error("API data is null. Unable to update gold ticker price");
                return;
            }
            Optional<GoldPrice> goldPriceOptional = goldPriceRepository.findById(GOLD_TICKER_ID);
            goldPriceOptional.ifPresent(goldTicker -> {
                BigDecimal newPrice = processedData.getPrice().setScale(2, RoundingMode.HALF_UP);
                updateTickerPrice(goldTicker, newPrice);
                kafkaService.sendMessage(topicConstants.getSendNotificationPriceTopic(), newPrice.toString());

                LOG.info("Gold ticker price updated successfully. New price: {}, Updated time: {}", newPrice, goldTicker.getUpdatedAt());
                updateGoldPrices();
            });
        } catch (Exception e) {
            LOG.error("Error updating gold ticker price: {}", e.getMessage(), e);
        }
    }

    private void updateTickerPrice(GoldPrice goldPrice, BigDecimal newPrice) {
        goldPrice.setCurrentPrice(newPrice);
        goldPrice.setUpdatedAt(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        goldPriceRepository.saveGoldPrice(goldPrice);
    }

    private void updateGoldPrices() {
        try {
            List<Integer> goldTypeIds = goldTypeRepository.allGoldTypeIds();
            for (int goldTypeId : goldTypeIds) {
                List<GoldPrice> pricesToUpdate = goldPriceRepository.findByTypeId(goldTypeId);
                pricesToUpdate.forEach(this::updateGoldPrice);
            }

        } catch (Exception e) {
            LOG.error("Error updating gold prices: {}", e.getMessage(), e);
        }
    }

    private void updateGoldPrice(GoldPrice goldPrice) {
        GoldType goldType = goldPrice.getGoldType();
        if (goldType.getName().equals(GOLD_TICKER)) {
            return;
        }

        BigDecimal newPrice = calculateNewGoldPrice(goldType.getNetWeight(), goldType.getCarat());
        goldPrice.setCurrentPrice(newPrice);
        goldPrice.setUpdatedAt(OffsetDateTime.now());
        goldPriceRepository.saveGoldPrice(goldPrice);
    }

    private BigDecimal calculateNewGoldPrice(BigDecimal netWeight, String carat) {
        return goldPriceCalculator.calculateGoldPrice(carat, netWeight);
    }
}

