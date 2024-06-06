package com.rahim.investmentservice.service.investment.implementation;

import com.rahim.investmentservice.enums.TransactionType;
import com.rahim.investmentservice.entity.Investment;
import com.rahim.investmentservice.entity.Transaction;
import com.rahim.investmentservice.service.investment.InvestmentDeletionService;
import com.rahim.investmentservice.service.repository.InvestmentRepositoryHandler;
import com.rahim.investmentservice.service.transaction.TxnCreationService;
import jakarta.persistence.EntityNotFoundException;
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

    private static final Logger log = LoggerFactory.getLogger(InvestmentDeletionImpl.class);
    private final InvestmentRepositoryHandler investmentRepositoryHandler;
    private final TxnCreationService txnCreationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellInvestment(int investmentId, int accountId, BigDecimal transactionAmount, BigDecimal purchaseAmount) {
        if (!investmentExists(investmentId, accountId)) {
            log.warn("Investment with ID {} does not exist for account with ID {}. Unable to sell.", investmentId, accountId);
            throw new EntityNotFoundException("Investment does not exist with ID: " + investmentId);
        }

        Investment investment = investmentRepositoryHandler.getInvestmentById(investmentId);
        Transaction txn = Transaction.builder()
                .accountId(accountId)
                .goldTypeId(investment.getGoldTypeId())
                .quantity(1)
                .transactionType(TransactionType.SELL.getValue())
                .transactionPrice(transactionAmount)
                .transactionDate(LocalDate.now(ZoneId.of("UTC")))
                .build();

        txnCreationService.addNewTransaction(txn);
        updateInvestmentPortfolio(investment, purchaseAmount);

    }

    private void updateInvestmentPortfolio(Investment investment, BigDecimal purchaseAmount) {
        int investmentId = investment.getId();

        if (investment.getQuantity() == 1) {
            investmentRepositoryHandler.deleteInvestment(investmentId);
            log.debug("Investment with ID {} deleted successfully after selling all units.", investmentId);
        } else {
            BigDecimal remainingPurchaseAmount = investment.getPurchasePrice().subtract(purchaseAmount);
            int remainingQuantity = investment.getQuantity() - 1;

            investment.setPurchasePrice(remainingPurchaseAmount);
            investment.setQuantity(remainingQuantity);
            investmentRepositoryHandler.saveInvestment(investment);

            log.debug("Remaining purchase amount for investment with ID {}: {}. Quantity decreased to {} after selling one unit.",
                    investmentId, remainingPurchaseAmount, remainingQuantity);
        }
    }

    private boolean investmentExists(int investmentId, int accountId) {
        return investmentRepositoryHandler.investmentExists(investmentId, accountId);
    }
}
