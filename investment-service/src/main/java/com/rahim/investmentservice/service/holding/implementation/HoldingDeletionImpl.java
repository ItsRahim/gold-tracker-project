package com.rahim.investmentservice.service.holding.implementation;

import com.rahim.investmentservice.model.Holding;
import com.rahim.investmentservice.service.holding.HoldingDeletionService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 23/05/2024
 */
@Service
@RequiredArgsConstructor
public class HoldingDeletionImpl implements HoldingDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingDeletionImpl.class);
    private final HoldingRepositoryHandler holdingRepositoryHandler;

    @Override
    public void sellHolding(int accountId, int holdingId) {
        Holding holding = holdingRepositoryHandler.getHoldingByIdAndAccountId(accountId, holdingId);

        if (holding == null) {
            LOG.warn("Holding with ID: {} does not exist for account with ID: {}. Unable to delete", holdingId, accountId);
            throw new EntityNotFoundException("Holding does not exist with ID: " + holdingId);
        }

    }
}
