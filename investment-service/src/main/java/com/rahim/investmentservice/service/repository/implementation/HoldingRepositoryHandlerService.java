package com.rahim.investmentservice.service.repository.implementation;

import com.rahim.investmentservice.model.Holding;
import com.rahim.investmentservice.repository.HoldingRepository;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Service
@Transactional
@RequiredArgsConstructor
public class HoldingRepositoryHandlerService implements HoldingRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingRepositoryHandlerService.class);
    private final HoldingRepository holdingRepository;

    @Override
    public void saveHolding(Holding holding) {
        if (holding != null) {
            try {
                LOG.debug("Attempting to save investment: {}", holding);
                holdingRepository.save(holding);
            } catch (DataAccessException e) {
                LOG.error("Failed to save holding: {} due to database error", holding, e);
                throw new RuntimeException("Failing to save holding with account id: " + holding.getAccountId());
            }
        } else {
            LOG.error("Holding information provided is null or contains null properties. Unable to save");
            throw new IllegalArgumentException("Holding information provided is null or contains null properties. Unable to save");
        }
    }

    @Override
    public void saveAllHoldings(List<Holding> holdings) {
        if (holdings != null && !holdings.isEmpty()) {
            try {
                LOG.debug("Attempting to save holdings: {}", holdings);
                holdingRepository.saveAll(holdings);
                LOG.debug("Successfully saved all holdings");
            } catch (DataAccessException e) {
                LOG.error("Failed to save holdings due to database error", e);
                throw new RuntimeException("Failing to save holdings");
            }
        } else {
            LOG.error("Holding list provided is null or empty. Unable to save");
            throw new IllegalArgumentException("Holding list provided is null or empty. Unable to save");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean holdingExistsById(int holdingId) {
        return holdingRepository.existsHoldingById(holdingId);
    }

    @Override
    public Holding getHoldingByIdAndAccountId(int holdingId, int accountId) {
        Optional<Holding> holdingOptional = holdingRepository.getHoldingByIdAndAccountId(holdingId, accountId);
        return holdingOptional.orElseGet(Holding::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Holding getHoldingById(int holdingId) {
        Optional<Holding> holdingOptional = holdingRepository.findById(holdingId);
        return holdingOptional.orElseGet(Holding::new);
    }
}
