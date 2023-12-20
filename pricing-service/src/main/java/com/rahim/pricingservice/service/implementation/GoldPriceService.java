package com.rahim.pricingservice.service.implementation;

import com.rahim.pricingservice.constant.TopicConstants;
import com.rahim.pricingservice.kafka.IKafkaService;
import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.model.GoldPrice;
import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldPriceRepository;
import com.rahim.pricingservice.service.IGoldPriceService;
import com.rahim.pricingservice.service.IGoldTypeService;
import com.rahim.pricingservice.util.ApiDataProcessor;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoldPriceService implements IGoldPriceService {
    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceService.class);
    private final GoldPriceRepository goldPriceRepository;
    private final IGoldTypeService goldTypeService;
    private final GoldPriceCalculator goldPriceCalculator;
    private final ApiDataProcessor apiDataProcessor;
    private final IKafkaService kafkaService;
    private static final String GOLD_TICKER = "XAUGBP";
    private static final int GOLD_TICKER_ID = 1;

    @Override
    public void updateGoldTickerPrice() {
        try {
            GoldData apiData = apiDataProcessor.getApiData();
            boolean isApiDataNull = ObjectUtils.anyNull(apiData);

            if (isApiDataNull) {
                throw new RuntimeException("API data is null. Unable to update gold ticker price.");
            }

            Optional<GoldPrice> goldPriceOptional = goldPriceRepository.findById(GOLD_TICKER_ID);

            if (goldPriceOptional.isPresent()) {
                OffsetDateTime updatedTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

                GoldPrice goldTicker = goldPriceOptional.get();

                BigDecimal newPrice = apiData.getPrice().setScale(2, RoundingMode.HALF_UP);

                goldTicker.setCurrentPrice(newPrice);
                goldTicker.setUpdatedAt(updatedTime);

                goldPriceRepository.save(goldTicker);

                LOG.info("Gold ticker price updated successfully. New price: {}, Updated time: {}", newPrice, updatedTime);

                List<Integer> idsToUpdate = goldTypeService.getAllIds();
                updateGoldPrices(idsToUpdate);
                kafkaService.sendMessage(TopicConstants.SEND_NOTIFICATION_PRICE_TOPIC, newPrice.toString());

            } else {
                LOG.warn("Gold ticker with ID 1 not found in the repository. Unable to update.");
            }
        } catch (Exception e) {
            LOG.error("Error updating gold ticker price: {}", e.getMessage(), e);
        }
    }

    private void updateGoldPrices(List<Integer> idsToUpdate) {
        try {
            int numOfUpdates = idsToUpdate.size();

            for (int id : idsToUpdate) {
                List<GoldPrice> pricesToUpdate = goldPriceRepository.findByGoldTypeId(id);

                for (GoldPrice priceToUpdate : pricesToUpdate) {
                    GoldType goldType = priceToUpdate.getGoldType();

                    if (goldType.getName().equals(GOLD_TICKER)) {
                        LOG.info("Skipping update for GoldType with name XAUGBP. GoldType ID: {}", goldType.getId());
                        numOfUpdates -= 1;
                        continue;
                    }

                    BigDecimal netWeight = goldType.getNetWeight();
                    String carat = goldType.getCarat();
                    BigDecimal newPrice = goldPriceCalculator.calculateGoldPrice(carat, netWeight);

                    priceToUpdate.setCurrentPrice(newPrice);
                    priceToUpdate.setUpdatedAt(OffsetDateTime.now());

                    goldPriceRepository.save(priceToUpdate);

                    LOG.info("Updated GoldPrice with ID {} for GoldType with ID {}. New Price: {}",
                            priceToUpdate.getId(), goldType.getId(), newPrice);
                }
            }

            LOG.info("Updated gold prices for {} Gold Types", numOfUpdates);

        } catch (Exception e) {
            LOG.error("Error updating gold prices: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<GoldPriceDTO> getGoldPrice(int goldId) {
        try {
            Optional<GoldPrice> goldPriceOptional = goldPriceRepository.findById(goldId);
            return goldPriceOptional.map(goldPrice -> new GoldPriceDTO(
                    goldPrice.getId(),
                    goldPrice.getGoldType().getName(),
                    goldPrice.getCurrentPrice(),
                    goldPrice.getUpdatedAt())
            );
        } catch (Exception e) {
            LOG.error("Error getting gold price with ID {}: {}", goldId, e.getMessage(), e);
            throw new RuntimeException("Error getting gold price", e);
        }
    }

    @Override
    public List<GoldPriceDTO> getAllGoldPrices() {
        try {
            List<GoldPrice> goldPrices = goldPriceRepository.findAll();

            return goldPrices.stream()
                    .map(goldPrice -> {
                        GoldType goldType = goldTypeService.findById(goldPrice.getGoldType().getId())
                                .orElseThrow(() -> new NoSuchElementException("GoldType not found"));

                        return new GoldPriceDTO(
                                goldPrice.getId(),
                                goldType.getName(),
                                goldPrice.getCurrentPrice(),
                                goldPrice.getUpdatedAt()
                        );
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error getting all gold prices: {}", e.getMessage(), e);
            throw new RuntimeException("Error getting all gold prices", e);
        }
    }

    @Override
    public void processNewGoldType(int goldTypeId) {
        try {
            LOG.info("Processing new gold type with ID: {}", goldTypeId);

            goldPriceRepository.insertGoldPrice(goldTypeId, BigDecimal.ZERO);

            LOG.info("Insert operation successful for gold type with ID: {}", goldTypeId);
        } catch (Exception e) {
            LOG.error("Error processing new gold type with ID {}: {}", goldTypeId, e.getMessage());
            throw new RuntimeException("Error processing new gold type", e);
        }
    }

    @Override
    @Transactional
    public void deleteGoldPrice(int goldTypeId) {
        try {
            Integer priceId = goldPriceRepository.getPriceIdByTypeId(goldTypeId);
            if (priceId != null) {
                goldPriceRepository.deleteById(priceId);
                LOG.info("Gold type with ID {} and associated price deleted successfully.", goldTypeId);
            } else {
                LOG.warn("Gold type with ID {} not found. Unable to delete associated price.", goldTypeId);
            }
        } catch (Exception e) {
            LOG.error("Error deleting gold type with ID {}: {}", goldTypeId, e.getMessage());
            throw new RuntimeException("Error deleting gold type", e);
        }
    }
}
