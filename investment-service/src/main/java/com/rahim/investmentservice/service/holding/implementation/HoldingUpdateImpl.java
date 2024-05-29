package com.rahim.investmentservice.service.holding.implementation;

import com.rahim.investmentservice.dao.HoldingDataAccess;
import com.rahim.investmentservice.dao.InvestmentsDataAccess;
import com.rahim.investmentservice.model.Holding;
import com.rahim.investmentservice.model.PriceUpdate;
import com.rahim.investmentservice.service.holding.HoldingUpdateService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import com.rahim.investmentservice.util.PriceUpdateRowMapper;
import com.rahim.investmentservice.util.ProfitLossUtil;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 27/05/2024
 */
@Configuration
@RequiredArgsConstructor
public class HoldingUpdateImpl implements HoldingUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingCreationImpl.class);
    private final HoldingRepositoryHandler holdingRepositoryHandler;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentValue(Integer goldTypeId, Double updatedPrice) {
        if (goldTypeId == null || updatedPrice == null) {
            LOG.error("Unable to update user holdings. Gold type ID and/or updated price is null.");
            return;
        }

        try {
            BigDecimal newPrice = BigDecimal.valueOf(updatedPrice);
            List<PriceUpdate> holdingsData = getHoldingInformation(goldTypeId);

            for (PriceUpdate priceUpdate : holdingsData) {
                Holding holding = holdingRepositoryHandler.getHoldingById(priceUpdate.getHoldingId());

                if (holding == null) {
                    LOG.warn("Holding not found for ID: {}", priceUpdate.getHoldingId());
                    continue;
                }

                BigDecimal originalPurchaseAmount = holding.getPurchaseAmount();
                BigDecimal profitLoss = ProfitLossUtil.calculateProfitLossPercentage(originalPurchaseAmount, newPrice);

                holding.setCurrentValue(newPrice);
                holding.setProfitLoss(profitLoss);

                holdingRepositoryHandler.saveHolding(holding);
                LOG.info("Updated holding ID: {} with new current value: {} and profit/loss: {}",
                        holding.getId(), newPrice, profitLoss);
            }
        } catch (Exception e) {
            LOG.error("Error updating current value for gold type ID: {}", goldTypeId, e);
            throw e;
        }
    }

    private List<PriceUpdate> getHoldingInformation(Integer goldTypeId) {
        String query = "SELECT h."
                + HoldingDataAccess.COL_HOLDING_ID
                + ", i." + InvestmentsDataAccess.COL_GOLD_TYPE_ID
                + ", h." + HoldingDataAccess.COL_TOTAL_PURCHASE_AMOUNT
                + ", h." + HoldingDataAccess.COL_CURRENT_VALUE
                + " FROM " + HoldingDataAccess.TABLE_NAME + " h"
                + " JOIN " + InvestmentsDataAccess.TABLE_NAME + " i"
                + " ON h." + InvestmentsDataAccess.COL_INVESTMENT_ID
                + " = i." + InvestmentsDataAccess.COL_INVESTMENT_ID
                + " WHERE i." + InvestmentsDataAccess.COL_GOLD_TYPE_ID + " = ?";

        return jdbcTemplate.query(query, new PriceUpdateRowMapper(), goldTypeId);
    }
}
