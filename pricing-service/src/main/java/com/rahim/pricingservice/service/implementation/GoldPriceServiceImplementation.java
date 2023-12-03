package com.rahim.pricingservice.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.pricingservice.model.GoldData;
import com.rahim.pricingservice.model.GoldPrice;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldPriceRepository;
import com.rahim.pricingservice.service.IGoldPriceService;
import com.rahim.pricingservice.service.IGoldTypeService;
import com.rahim.pricingservice.util.GoldPriceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Setter
@Service
@RequiredArgsConstructor
public class GoldPriceServiceImplementation implements IGoldPriceService {
    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceServiceImplementation.class);
    private final GoldPriceRepository goldPriceRepository;
    private final IGoldTypeService goldTypeService;
    private final GoldPriceCalculator goldPriceCalculator;
    private final String GOLD_TICKER = "XAUGBP";
    private GoldData apiData;
    private String kafkaData;


    @Override
    public void setKafkaData(String data) {
        this.kafkaData = data;
        processApiData();
    }

    public void processApiData() {
        try {
            apiData = new GoldData(kafkaData);
            goldPriceCalculator.calculatePricePerGram(apiData.getPrice());
            updateGoldTickerPrice();
        } catch (JsonProcessingException e) {
            LOG.error("Error processing API data: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("Unexpected error processing API data: {}", e.getMessage(), e);
        }
    }

    @Override
    public void updateGoldTickerPrice() {
        try {
            Optional<GoldPrice> goldPriceOptional = goldPriceRepository.findById(1);

            if (goldPriceOptional.isPresent()) {
                OffsetDateTime updatedTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

                GoldPrice goldTicker = goldPriceOptional.get();

                BigDecimal newPrice = apiData.getPrice().setScale(2, RoundingMode.HALF_UP);

                goldTicker.setCurrentPrice(newPrice);
                goldTicker.setUpdatedAt(updatedTime);

                goldPriceRepository.save(goldTicker);

                LOG.info("Gold ticker price updated successfully. New price: {}, Updated time: {}", newPrice, updatedTime);

                updateGoldPrices();

            } else {
                LOG.warn("Gold ticker with ID 1 not found in the repository. Unable to update.");
            }
        } catch (Exception e) {
            LOG.error("Error updating gold ticker price: {}", e.getMessage(), e);
        }
    }

    private void updateGoldPrices() {
        try {
            List<Integer> idsToUpdate = goldTypeService.getAllIds();
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
    public Optional<GoldPrice> getGoldPrice(int goldId) {
        try {
            Optional<GoldPrice> goldPrice = goldPriceRepository.findById(goldId);

            if (goldPrice.isPresent()) {
                LOG.info("Found gold item with ID: {}", goldId);
            } else {
                LOG.info("Gold item with ID: {} Not found", goldId);
            }

            return goldPrice;
        } catch (DataAccessException e) {
            LOG.error("Error fetching gold item with ID: {}", goldId, e);
            return Optional.empty();
        }
    }
}
