package com.rahim.investmentservice.service.holding.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.common.service.http.HttpClientService;
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

/**
 * @author Rahim Ahmed
 * @created 21/05/2024
 */
@Service
@RequiredArgsConstructor
public class HoldingCreationImpl implements HoldingCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingCreationImpl.class);
    private final HoldingRepositoryHandler holdingRepositoryHandler;
    private final HttpClientService httpClientService;
    private final ObjectMapper objectMapper;


    private static final String PRICING_SERVICE = "PRICING-SERVICE";

    @Override
    public void processNewHolding(Holding holding, int goldTypeId, int quantity) {

        BigDecimal currentValue = getCurrentValue(goldTypeId);
        BigDecimal profileLoss = calculateProfitLossPercentage(holding.getTotalPurchaseAmount(), currentValue);

        holding.setCurrentValue(currentValue);
        holding.setProfitLoss(profileLoss);

        holdingRepositoryHandler.saveHolding(holding);
    }

    private BigDecimal calculateProfitLossPercentage(BigDecimal totalPurchaseAmount, BigDecimal currentValue) {
        if (totalPurchaseAmount == null || currentValue == null || totalPurchaseAmount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Total purchase amount must not be null or zero, and current value must not be null");
        }

        BigDecimal profitLoss = currentValue.subtract(totalPurchaseAmount);
        return profitLoss.divide(totalPurchaseAmount, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getCurrentValue(int goldTypeId) {
        try {
            ResponseEntity<String> response = httpClientService
                    .callService(PRICING_SERVICE, "/api/v1/gold/pricing-service/gold-price/{goldTypeId}",
                            String.class, 26);

            if (response.getStatusCode().is2xxSuccessful()) {
                String jsonResponse = response.getBody();
                LOG.debug("Received response: {}", jsonResponse);

                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                JsonNode currentPriceNode = jsonNode.get("currentPrice");

                if (currentPriceNode != null && currentPriceNode.isNumber()) {
                    BigDecimal currentPrice = currentPriceNode.decimalValue();
                    LOG.debug("Current price for goldTypeId {}: {}", goldTypeId, currentPrice);
                    return currentPrice;
                } else {
                    LOG.error("Error parsing current price from response: {}", jsonResponse);
                    throw new RuntimeException("Error parsing current price from response");
                }
            } else {
                LOG.error("Error retrieving gold price, status code: {}", response.getStatusCode());
                throw new RuntimeException("Error retrieving gold price");
            }
        } catch (JsonProcessingException e) {
            LOG.error("Error processing JSON response for goldTypeId {}: {}", goldTypeId, e.getMessage(), e);
            throw new RuntimeException("Error processing JSON response", e);
        } catch (Exception e) {
            LOG.error("Error retrieving gold price for goldTypeId {}: {}", goldTypeId, e.getMessage(), e);
            throw new RuntimeException("Error retrieving gold price", e);
        }
    }

}
