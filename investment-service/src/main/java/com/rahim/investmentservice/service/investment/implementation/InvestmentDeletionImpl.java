package com.rahim.investmentservice.service.investment.implementation;

import com.rahim.investmentservice.enums.TransactionType;
import com.rahim.investmentservice.model.Investment;
import com.rahim.investmentservice.model.Transaction;
import com.rahim.investmentservice.service.investment.InvestmentDeletionService;
import com.rahim.investmentservice.service.repository.InvestmentRepositoryHandler;
import com.rahim.investmentservice.service.transaction.TxnCreationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Rahim Ahmed
 * @created 21/05/2024
 */
@Service
@RequiredArgsConstructor
public class InvestmentDeletionImpl implements InvestmentDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentDeletionImpl.class);
    private final InvestmentRepositoryHandler investmentRepositoryHandler;
    private final TxnCreationService txnCreationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellInvestment(int accountId, int investmentId) {
        if (!investmentExists(accountId, investmentId)) {
            LOG.warn("Investment for ID: {} does not exist for account with ID: {}", investmentId, accountId);
        }

        Investment investment = investmentRepositoryHandler.getInvestmentById(investmentId);
        Transaction transaction = Transaction.builder()
                .accountId(accountId)
                .goldTypeId(investment.getGoldTypeId())
                .quantity(investment.getQuantity())
                .transactionType(TransactionType.SELL.getValue())
                .transactionPrice(BigDecimal.ZERO)
                .transactionDate(LocalDate.now(ZoneId.of("UTC")))
                .build();

        txnCreationService.addNewTransaction(transaction);

        if (investment.getQuantity() == 1) {
            //remove row from txnTable
        }

    }

    private boolean investmentExists(int accountId, int investmentId) {
        return investmentRepositoryHandler.investmentExists(accountId, investmentId);
    }
}
