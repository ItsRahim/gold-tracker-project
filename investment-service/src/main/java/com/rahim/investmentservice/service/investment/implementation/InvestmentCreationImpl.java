package com.rahim.investmentservice.service.investment.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.common.util.DateTimeUtil;
import com.rahim.investmentservice.request.InvestmentRequest;
import com.rahim.investmentservice.enums.TransactionType;
import com.rahim.investmentservice.entity.Holding;
import com.rahim.investmentservice.entity.Investment;
import com.rahim.investmentservice.entity.Transaction;
import com.rahim.investmentservice.service.holding.HoldingCreationService;
import com.rahim.investmentservice.service.investment.InvestmentCreationService;
import com.rahim.investmentservice.service.repository.InvestmentRepositoryHandler;
import com.rahim.investmentservice.service.transaction.TxnCreationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Author: Rahim Ahmed
 * Created: 20/05/2024
 */
@Service
@RequiredArgsConstructor
public class InvestmentCreationImpl implements InvestmentCreationService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentCreationImpl.class);
    private final InvestmentRepositoryHandler investmentRepositoryHandler;
    private final HoldingCreationService holdingCreationService;
    private final TxnCreationService txnCreationService;
    private final CacheManager hazelcastCacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNewInvestment(int accountId, InvestmentRequest investmentRequest) {
        validateInvestmentRequest(accountId, investmentRequest);

        final String goldType = investmentRequest.getGoldTypeName();
        final Integer quantity = getValidQuantity(investmentRequest.getQuantity());
        final BigDecimal totalPurchasePrice = investmentRequest.getTotalPurchasePrice();
        LocalDate purchaseDate = getValidPurchaseDate(investmentRequest.getPurchaseDate());

        final Integer goldTypeId = getGoldTypeId(goldType);

        log.debug("Adding new investment for account ID: {}, gold type ID: {}, quantity: {}, purchase price: {}, purchase date: {}",
                accountId, goldTypeId, quantity, totalPurchasePrice, purchaseDate);

        Investment investment = new Investment(accountId, goldTypeId, quantity, totalPurchasePrice, purchaseDate);
        investmentRepositoryHandler.saveInvestment(investment);

        log.debug("Investment saved successfully for account ID: {}", accountId);

        Holding holding = new Holding(accountId, investment.getId());
        holdingCreationService.processNewHolding(holding, goldTypeId, totalPurchasePrice, quantity);

        Transaction transaction = new Transaction(accountId, goldTypeId, quantity, TransactionType.BUY.getValue(), totalPurchasePrice, purchaseDate);
        txnCreationService.addNewTransaction(transaction);

        log.debug("Transaction created successfully for account ID: {}", accountId);
    }

    private void validateInvestmentRequest(int accountId, InvestmentRequest investmentRequest) {
        if (investmentRequest == null) {
            log.error("Investment request is null");
            throw new ValidationException("Investment request cannot be null");
        }

        if (investmentRequest.getTotalPurchasePrice() == null || investmentRequest.getTotalPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Invalid purchase price: {}", investmentRequest.getTotalPurchasePrice());
            throw new ValidationException("Invalid purchase price provided");
        }

        if (!accountExists(accountId)) {
            log.warn("Account does not exist for ID: {}", accountId);
            throw new ValidationException("Account for ID: " + accountId + " does not exist");
        }

        final String goldType = investmentRequest.getGoldTypeName();
        if (getGoldTypeId(goldType) == null) {
            log.warn("Gold type does not exist: {}", goldType);
            throw new ValidationException("Gold type: " + goldType + " does not exist");
        }
    }

    private boolean accountExists(int accountId) {
        ISet<Integer> accountIds = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_SET);
        boolean exists = accountIds.contains(accountId);
        log.debug("Account existence check for ID: {}. Exists: {}", accountId, exists);
        return exists;
    }

    private Integer getGoldTypeId(String goldType) {
        IMap<String, Integer> goldTypeMap = hazelcastCacheManager.getMap(HazelcastConstant.GOLD_TYPE_MAP);
        Integer goldTypeId = goldTypeMap.get(goldType);
        log.debug("Gold type ID lookup for type: {}. Found ID: {}", goldType, goldTypeId);
        return goldTypeId;
    }

    private int getValidQuantity(Integer quantity) {
        if (quantity == null || quantity == 0) {
            log.warn("Quantity is null or zero, defaulting to 1");
            return 1;
        }
        return quantity;
    }

    private LocalDate getValidPurchaseDate(LocalDate purchaseDate) {
        if (purchaseDate == null) {
            log.warn("No purchase date provided. Assuming purchase was made today");
            return DateTimeUtil.getLocalDate();
        }
        return purchaseDate;
    }
}
