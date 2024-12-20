package com.rahim.pricingservice.service.price.implementation;

import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.IKafkaService;
import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.entity.GoldPrice;
import com.rahim.pricingservice.entity.GoldType;
import com.rahim.common.model.kafka.GoldTypePrice;
import com.rahim.pricingservice.service.price.IGoldPriceUpdateService;
import com.rahim.pricingservice.service.repository.IGoldPriceRepositoryHandler;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.rahim.common.util.JsonUtil.convertObjectToJson;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GoldPriceUpdateService implements IGoldPriceUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceUpdateService.class);
    private final IGoldPriceRepositoryHandler goldPriceRepository;
    private final IGoldTypeRepositoryHandler goldTypeRepository;
    private final GoldPriceCalculator goldPriceCalculator;
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

            GoldPrice goldPrice = goldPriceRepository.findById(GOLD_TICKER_ID);
            BigDecimal newPrice = processedData.getPrice().setScale(2, RoundingMode.HALF_UP);

            updateTickerPrice(goldPrice, newPrice);

            String priceData = String.valueOf(newPrice);
            kafkaService.sendMessage(KafkaTopic.THRESHOLD_PRICE_UPDATE, priceData);

            LOG.info("Gold ticker price updated successfully. New price: {}, Updated time: {}", newPrice, goldPrice.getUpdatedAt());
            updateGoldPrices();

        } catch (Exception e) {
            LOG.error("Error updating gold ticker price: {}", e.getMessage(), e);
        }
    }

    private void updateTickerPrice(GoldPrice goldPrice, BigDecimal newPrice) {
        goldPrice.setCurrentPrice(newPrice);
        goldPriceRepository.saveGoldPrice(goldPrice);
    }

    private void updateGoldPrices() {
        try {
            List<Integer> goldTypeIds = goldTypeRepository.allGoldTypeIds();
            for (int goldTypeId : goldTypeIds) {
                List<GoldPrice> pricesToUpdate = goldPriceRepository.findByTypeId(goldTypeId);
                pricesToUpdate.forEach(this::updateGoldPrice);
            }
            LOG.info("Gold prices updated successfully");
        } catch (Exception e) {
            LOG.error("Error updating gold prices: {}", e.getMessage(), e);
        }
    }

    private void updateGoldPrice(GoldPrice goldPrice) {
        GoldType goldType = goldPrice.getGoldType();
        if (goldType.getName().equals(GOLD_TICKER)) {
            LOG.debug("Skipping update for gold ticker: {}", goldPrice);
            return;
        }

        BigDecimal newPrice = calculateNewGoldPrice(goldType.getNetWeight(), goldType.getCarat());
        goldPrice.setCurrentPrice(newPrice);
        goldPriceRepository.saveGoldPrice(goldPrice);
        sendInvestmentUpdate(goldPrice.getId(), goldPrice.getCurrentPrice());
    }

    private BigDecimal calculateNewGoldPrice(BigDecimal netWeight, String carat) {
        return goldPriceCalculator.calculateGoldPrice(carat, netWeight);
    }

    private void sendInvestmentUpdate(Integer id, BigDecimal currentPrice) {
        GoldTypePrice update = new GoldTypePrice(id.toString(), currentPrice);
        String json = convertObjectToJson(update);
        kafkaService.sendMessage(KafkaTopic.HOLDING_PRICE_UPDATE, json);
    }
}

