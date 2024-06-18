package com.rahim.investmentservice.service.repository.implementation;

import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.common.exception.ValidationException;
import com.rahim.investmentservice.dao.HoldingDataAccess;
import com.rahim.investmentservice.dao.InvestmentsDataAccess;
import com.rahim.investmentservice.entity.Investment;
import com.rahim.investmentservice.model.InvestmentResponse;
import com.rahim.investmentservice.repository.InvestmentRepository;
import com.rahim.investmentservice.service.repository.InvestmentRepositoryHandler;
import com.rahim.investmentservice.util.InvestmentRowMapper;
import com.rahim.investmentservice.util.ProfitLossUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Service
@RequiredArgsConstructor
public class InvestmentRepositoryHandlerService implements InvestmentRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentRepositoryHandlerService.class);
    private final InvestmentRepository investmentRepository;
    private final JdbcTemplate jdbcTemplate;

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

    @Override
    public List<InvestmentResponse> getInvestmentByAccountId(int accountId) {
        String query = "SELECT "
                + "(SELECT SUM(h." + HoldingDataAccess.COL_CURRENT_VALUE + ") FROM " + HoldingDataAccess.TABLE_NAME + " h"
                + " WHERE h." + HoldingDataAccess.COL_INVESTMENT_ID + " = i." + InvestmentsDataAccess.COL_INVESTMENT_ID
                + " AND h." + HoldingDataAccess.COL_ACCOUNT_ID + " = i." + InvestmentsDataAccess.COL_ACCOUNT_ID + ") AS total_current_value, "
                + "i." + InvestmentsDataAccess.COL_PURCHASE_PRICE + ", "
                + "i." + InvestmentsDataAccess.COL_PURCHASE_DATE + " "
                + "FROM " + InvestmentsDataAccess.TABLE_NAME + " i "
                + "WHERE i." + InvestmentsDataAccess.COL_ACCOUNT_ID + " = ?";

        List<InvestmentResponse> investmentResponses = jdbcTemplate.query(query, new InvestmentRowMapper(), accountId);
        for (InvestmentResponse investmentResponse : investmentResponses) {
            BigDecimal profitLossPercentage = ProfitLossUtil.calculateProfitLossPercentage(investmentResponse.getPurchasePrice(), investmentResponse.getCurrentValue());
            investmentResponse.setProfitLoss(profitLossPercentage);
        }

        return investmentResponses;
    }

}
