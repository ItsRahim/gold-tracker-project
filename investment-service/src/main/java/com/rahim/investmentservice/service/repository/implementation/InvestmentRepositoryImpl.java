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

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Service
@Transactional
@RequiredArgsConstructor
public class InvestmentRepositoryImpl implements InvestmentRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentRepositoryImpl.class);
    private final InvestmentRepository investmentRepository;

    @Override
    public void save(Investment investment) {
        try {
            LOG.debug("Attempting to save investment: {}", investment);
            investmentRepository.save(investment);
        } catch (DataAccessException e) {
            LOG.error("Failed to save investment: {} due to database error", investment, e);
            throw new RuntimeException("Failing to save investment with account id: " + investment.getAccountId());
        } catch (Exception e) {
            LOG.error("Unexpected error occurred while saving investment: {}", investment, e);
            throw new RuntimeException("Unexpected error occurred while saving investment");
        }
    }
}
