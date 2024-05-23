package com.rahim.investmentservice.service.investment.implementation;

import com.rahim.investmentservice.enums.TransactionType;
import com.rahim.investmentservice.model.Investment;
import com.rahim.investmentservice.model.Transaction;
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

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentDeletionImpl.class);
    private final InvestmentRepositoryHandler investmentRepositoryHandler;
    private final TxnCreationService txnCreationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellInvestment(int accountId, int investmentId, BigDecimal purchaseAmount) {
        if (!investmentExists(accountId, investmentId)) {
            LOG.warn("Investment with ID {} does not exist for account with ID {}. Unable to sell.", investmentId, accountId);
            throw new EntityNotFoundException("Investment does not exist with ID: " + investmentId);
        }

        Investment investment = investmentRepositoryHandler.getInvestmentById(investmentId);
        Transaction transaction = Transaction.builder()
                .accountId(accountId)
                .goldTypeId(investment.getGoldTypeId())
                .quantity(investment.getQuantity())
                .transactionType(TransactionType.SELL.getValue())
                .transactionPrice(purchaseAmount)
                .transactionDate(LocalDate.now(ZoneId.of("UTC")))
                .build();

        txnCreationService.addNewTransaction(transaction);

        if (investment.getQuantity() == 1) {
            investmentRepositoryHandler.deleteInvestment(investmentId);
            LOG.debug("Investment with ID {} deleted successfully after selling all units.", investmentId);
        } else {
            BigDecimal remainingPurchaseAmount = investment.getPurchasePrice().subtract(purchaseAmount);
            int remainingQuantity = investment.getQuantity() - 1;

            investment.setPurchasePrice(remainingPurchaseAmount);
            investment.setQuantity(remainingQuantity);
            investmentRepositoryHandler.saveInvestment(investment);

            LOG.debug("Remaining purchase amount for investment with ID {}: {}. Quantity decreased to {} after selling one unit.",
                    investmentId, remainingPurchaseAmount, remainingQuantity);
        }
    }

    private boolean investmentExists(int accountId, int investmentId) {
        return investmentRepositoryHandler.investmentExists(accountId, investmentId);
    }
}
