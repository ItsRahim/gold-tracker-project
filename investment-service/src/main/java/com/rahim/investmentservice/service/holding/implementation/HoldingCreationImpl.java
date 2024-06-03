package com.rahim.investmentservice.service.holding.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.JsonServiceException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.http.HttpService;
import com.rahim.investmentservice.feign.PricingServiceClient;
import com.rahim.investmentservice.entity.Holding;
import com.rahim.investmentservice.service.holding.HoldingCreationService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import com.rahim.investmentservice.util.ProfitLossUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Rahim Ahmed
 * @created 21/05/2024
 */
@Service
@RequiredArgsConstructor
public class HoldingCreationImpl implements HoldingCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingCreationImpl.class);
    private final HoldingRepositoryHandler holdingRepositoryHandler;
    private final PricingServiceClient pricingServiceClient;
    private final HttpService httpService;

    @Override
    public void processNewHolding(Holding holding, int goldTypeId, BigDecimal purchasePrice, int quantity) {
        try {
            BigDecimal currentValue = getCurrentValue(goldTypeId);
            BigDecimal purchaseAmount = calculateIndividualAmount(purchasePrice, quantity);
            BigDecimal profileLoss = ProfitLossUtil.calculateProfitLossPercentage(purchaseAmount, currentValue);

            holding.setPurchaseAmount(purchaseAmount);
            holding.setCurrentValue(currentValue);
            holding.setProfitLoss(profileLoss);

            List<Holding> holdings = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                holdings.add(new Holding(holding));
            }

            holdingRepositoryHandler.saveAllHoldings(holdings);

            LOG.info("New holding processed successfully: {}", holding);
        } catch (Exception e) {
            LOG.error("Error processing new holding: {}", e.getMessage(), e);
            throw new DatabaseException("An error occurred attempting to process and save new holding");
        }

    }

    private BigDecimal calculateIndividualAmount(BigDecimal purchasePrice, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be a positive integer");
        }

        return purchasePrice.divide(BigDecimal.valueOf(quantity), 4, RoundingMode.HALF_UP);
    }

    public BigDecimal getCurrentValue(int goldTypeId) {
        LOG.debug("Fetching current value for gold type ID: {}", goldTypeId);

        Supplier<ResponseEntity<String>> pricingServiceCall = () -> pricingServiceClient.getGoldPrice(goldTypeId);

        return httpService.fetchValueFromService(pricingServiceCall, jsonResponse -> {
            JsonNode currentPriceNode = jsonResponse.get("currentPrice");
            if (currentPriceNode != null && currentPriceNode.isNumber()) {
                BigDecimal currentValue = currentPriceNode.decimalValue();
                LOG.debug("Current value for gold type ID {}: {}", goldTypeId, currentValue);
                return currentValue;
            }
            throw new JsonServiceException("Error parsing current price from response");
        });
    }

}
