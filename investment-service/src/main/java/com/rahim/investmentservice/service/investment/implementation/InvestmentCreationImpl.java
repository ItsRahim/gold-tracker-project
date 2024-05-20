package com.rahim.investmentservice.service.investment.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.investmentservice.dto.InvestmentRequestDto;
import com.rahim.investmentservice.enums.TransactionType;
import com.rahim.investmentservice.model.Investment;
import com.rahim.investmentservice.model.Transaction;
import com.rahim.investmentservice.service.investment.InvestmentCreationService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import com.rahim.investmentservice.service.transaction.TxnCreationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author Rahim Ahmed
 * @created 20/05/2024
 */
@Service
@RequiredArgsConstructor
public class InvestmentCreationImpl implements InvestmentCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentCreationImpl.class);
    private final HoldingRepositoryHandler holdingRepositoryHandler;
    private final TxnCreationService txnCreationService;
    private final CacheManager hazelcastCacheManager;

    @Override
    public void addNewHolding(int accountId, InvestmentRequestDto investmentRequestDto) {
        validateRequest(investmentRequestDto);

        final String goldType = investmentRequestDto.getGoldTypeName();
        final Integer quantity = investmentRequestDto.getQuantity();
        final BigDecimal purchasePrice = investmentRequestDto.getPurchasePrice();
        LocalDate purchaseDate = investmentRequestDto.getPurchaseDate();

        if (!accountExists(accountId)) {
            LOG.warn("Unable to add transaction for account id: {}. Account does not exist", accountId);
            throw new IllegalStateException("Account for ID: " + accountId + " does not exist");
        }

        final Integer goldTypeId = getGoldTypeId(goldType);
        if (goldTypeId == null) {
            LOG.warn("Unable to add transaction for gold type: {}. Gold type does not exist", goldType);
            throw new IllegalStateException("Gold type: " + goldType + " does not exist");
        }

        final int quantityValue = (quantity == null || quantity == 0) ? 1 : quantity;

        if (purchaseDate == null) {
            LOG.warn("No purchase date provided. Assuming purchase was made today");
            purchaseDate = LocalDate.now(ZoneId.of("UTC"));
        }

        Investment investment = new Investment(accountId, goldTypeId, quantityValue, purchasePrice, purchaseDate);
        holdingRepositoryHandler.save(investment);

        OffsetDateTime offsetDateTime = purchaseDate.atStartOfDay().atOffset(ZoneOffset.UTC);
        Transaction transaction = new Transaction(accountId, goldTypeId, quantity, TransactionType.BUY, purchasePrice, offsetDateTime);
        txnCreationService.addNewTransaction(transaction);
    }

    private void validateRequest(InvestmentRequestDto investmentRequestDto) {
        if (investmentRequestDto == null) {
            throw new IllegalArgumentException("Investment request cannot be null");
        }
        if (investmentRequestDto.getPurchasePrice() == null || investmentRequestDto.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            LOG.warn("Unable to process transaction. Transaction price is null or non-positive");
            throw new IllegalStateException("Invalid purchase price provided");
        }
    }

    private boolean accountExists(int accountId) {
        ISet<Integer> accountIds = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_SET);
        return accountIds.contains(accountId);
    }

    private Integer getGoldTypeId(String goldType) {
        IMap<String, Integer> goldTypeMap = hazelcastCacheManager.getMap(HazelcastConstant.GOLD_TYPE_MAP);
        return goldTypeMap.get(goldType);
    }
}
