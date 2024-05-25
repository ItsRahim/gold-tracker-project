package com.rahim.investmentservice.service.repository.implementation;

import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.common.exception.ValidationException;
import com.rahim.investmentservice.model.Investment;
import com.rahim.investmentservice.repository.InvestmentRepository;
import com.rahim.investmentservice.service.repository.InvestmentRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Service
@RequiredArgsConstructor
public class InvestmentRepositoryHandlerService implements InvestmentRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentRepositoryHandlerService.class);
    private final InvestmentRepository investmentRepository;

    @Override
    @Transactional
    public void saveInvestment(Investment investment) {
        if (investment == null) {
            LOG.error("Investment information provided is null or contains null properties. Unable to save");
            throw new ValidationException("Investment information provided is null or contains null properties. Unable to save");
        }

        try {
            investmentRepository.save(investment);
            LOG.debug("Saved investment: {}", investment);
        } catch (DataAccessException e) {
            LOG.error("Failed to save investment due to database error: {}", e.getMessage(), e);
            throw new DatabaseException("Failing to save investment with account id: " + investment.getAccountId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean investmentExists(int investmentId, int accountId) {
        return investmentRepository.existsInvestmentByAccountId(investmentId, accountId);
    }

    @Override
    public Investment getInvestmentById(int investmentId) {
        return investmentRepository.getInvestmentById(investmentId)
                .orElseThrow(() -> new EntityNotFoundException("Investment does not exist for ID " + investmentId));
    }

    @Override
    public void deleteInvestment(int investmentId) {
        getInvestmentById(investmentId);
        investmentRepository.deleteById(investmentId);
    }
}
