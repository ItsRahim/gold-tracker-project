package com.rahim.investmentservice.service.holding.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.rahim.common.service.http.HttpService;
import com.rahim.investmentservice.feign.PricingServiceClient;
import com.rahim.investmentservice.model.Holding;
import com.rahim.investmentservice.service.holding.HoldingCreationService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
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
    public void processNewHolding(Holding holding, int goldTypeId, int quantity) {
        BigDecimal currentValue = getCurrentValue(goldTypeId);
        BigDecimal profileLoss = calculateProfitLossPercentage(holding.getTotalPurchaseAmount(), currentValue);

        holding.setCurrentValue(currentValue);
        holding.setProfitLoss(profileLoss);

        List<Holding> holdings = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            holdings.add(new Holding(holding));
        }

        holdingRepositoryHandler.saveAllHoldings(holdings);

        LOG.info("New holding processed successfully: {}", holding);
    }

    private BigDecimal calculateProfitLossPercentage(BigDecimal totalPurchaseAmount, BigDecimal currentValue) {
        if (totalPurchaseAmount == null || currentValue == null || totalPurchaseAmount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Total purchase amount must not be null or zero, and current value must not be null");
        }

        BigDecimal profitLoss = currentValue.subtract(totalPurchaseAmount);
        return profitLoss.divide(totalPurchaseAmount, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getCurrentValue(int goldTypeId) {
        LOG.debug("Fetching current value for gold type ID: {}", goldTypeId);
        Supplier<ResponseEntity<String>> pricingServiceCall = () -> pricingServiceClient.getGoldPrice(goldTypeId);

        return httpService.fetchValueFromService(pricingServiceCall,
                jsonResponse -> {
                    JsonNode currentPriceNode = jsonResponse.get("currentPrice");
                    if (currentPriceNode != null && currentPriceNode.isNumber()) {
                        BigDecimal currentValue = currentPriceNode.decimalValue();
                        LOG.debug("Current value for gold type ID {}: {}", goldTypeId, currentValue);
                        return currentValue;
                    }
                    throw new RuntimeException("Error parsing current price from response");
                });
    }
}
