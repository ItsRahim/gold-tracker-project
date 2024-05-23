package com.rahim.investmentservice.service.holding.implementation;

import com.rahim.investmentservice.model.Holding;
import com.rahim.investmentservice.service.holding.HoldingDeletionService;
import com.rahim.investmentservice.service.investment.InvestmentDeletionService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 23/05/2024
 */
@Service
@RequiredArgsConstructor
public class HoldingDeletionImpl implements HoldingDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingDeletionImpl.class);
    private final HoldingRepositoryHandler holdingRepositoryHandler;
    private final InvestmentDeletionService investmentDeletionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellHolding(List<Integer> holdingIds, int accountId) {
        LOG.debug("Initiating bulk sale of holdings for account with ID {}", accountId);

        for (int holdingId : holdingIds) {
            Holding holding = holdingRepositoryHandler.getHoldingByIdAndAccountId(holdingId, accountId);

            if (holding == null) {
                LOG.warn("Holding with ID {} does not exist for account with ID {}. Skipping.", holdingId, accountId);
                continue;
            }

            LOG.debug("Holding with ID {} found for account with ID {}. Proceeding with sale.", holdingId, accountId);

            int investmentId = holding.getInvestmentId();
            BigDecimal transactionAmount = holding.getCurrentValue().negate();
            BigDecimal purchaseAmount = holding.getPurchaseAmount();

            deleteHolding(holdingId);
            investmentDeletionService.sellInvestment(investmentId, accountId, transactionAmount, purchaseAmount);

            LOG.info("Holding with ID {} for account with ID {} sold successfully.", holdingId, accountId);
        }
    }

    private void deleteHolding(int holdingId) {
        LOG.debug("Attempting to delete holding with ID: {}", holdingId);
        holdingRepositoryHandler.deleteHolding(holdingId);
    }
}
