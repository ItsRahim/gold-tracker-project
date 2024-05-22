package com.rahim.investmentservice.service.repository.implementation;

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
        if (investment != null) {
            try {
                LOG.debug("Attempting to save investment: {}", investment);
                investmentRepository.save(investment);
            } catch (DataAccessException e) {
                LOG.error("Failed to save investment: {} due to database error", investment, e);
                throw new RuntimeException("Failing to save investment with account id: " + investment.getAccountId());
            }
        } else {
            LOG.error("Investment information provided is null or contains null properties. Unable to save");
            throw new IllegalArgumentException("Investment information provided is null or contains null properties. Unable to save");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean investmentExists(int accountId, int investmentId) {
        return investmentRepository.existsInvestmentByAccountId(accountId, investmentId);
    }

    @Override
    public Investment getInvestmentById(int investmentId) {
        Optional<Investment> investmentOptional = investmentRepository.getInvestmentById(investmentId);
        return investmentOptional.orElseGet(Investment::new);
    }
}
