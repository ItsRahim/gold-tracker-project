package com.rahim.investmentservice.service.repository.implementation;

import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.common.exception.ValidationException;
import com.rahim.investmentservice.entity.Holding;
import com.rahim.investmentservice.repository.HoldingRepository;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        if (holding == null) {
            LOG.error("Holding information provided is null or contains null properties. Unable to save");
            throw new ValidationException("Holding information provided is null or contains null properties. Unable to save");
        }

        try {
            LOG.debug("Attempting to save investment: {}", holding);
            holdingRepository.save(holding);
        } catch (DataAccessException e) {
            LOG.error("Failed to save holding due to database error: {}", e.getMessage(), e);
            throw new DatabaseException("Failing to save holding with account id: " + holding.getAccountId());
        }
    }

    @Override
    public void saveAllHoldings(List<Holding> holdings) {
        if (holdings == null || holdings.isEmpty()) {
            LOG.error("Holding list provided is null or empty. Unable to save");
            throw new ValidationException("Holding list provided is null or empty. Unable to save");
        }
        try {
            holdingRepository.saveAll(holdings);
            LOG.debug("Successfully saved all holdings");
        } catch (DataAccessException e) {
            LOG.error("Failed to save holdings due to database error", e);
            throw new DatabaseException("Failing to save holdings");
        }
    }

    @Override
    public void deleteHolding(int holdingId) {
        holdingRepository.deleteById(holdingId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean holdingExistsById(int holdingId) {
        return holdingRepository.existsHoldingById(holdingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Holding getHoldingByIdAndAccountId(int holdingId, int accountId) {
        return holdingRepository.getHoldingByIdAndAccountId(holdingId, accountId)
                .orElseThrow(() -> new EntityNotFoundException("Holding for given holding/account id does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public Holding getHoldingById(int holdingId) {
        return holdingRepository.findById(holdingId)
                .orElseThrow(() -> new EntityNotFoundException("Holding for given holding id does not exist"));
    }

    @Override
    public List<Holding> getHoldingsByAccountId(int accountId) {
        return holdingRepository.getHoldingByAccountId(accountId);
    }
}
