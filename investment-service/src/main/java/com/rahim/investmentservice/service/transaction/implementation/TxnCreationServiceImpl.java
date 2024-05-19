package com.rahim.investmentservice.service.transaction.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.investmentservice.dto.TxnRequestDto;
import com.rahim.investmentservice.enums.TransactionType;
import com.rahim.investmentservice.model.Transaction;
import com.rahim.investmentservice.service.repository.TxnRepositoryHandler;
import com.rahim.investmentservice.service.transaction.TxnCreationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Service
@RequiredArgsConstructor
public class TxnCreationServiceImpl implements TxnCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(TxnCreationServiceImpl.class);
    private final TxnRepositoryHandler txnRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    @Override
    public void addNewTransaction(int accountId, TxnRequestDto txnRequestDto) {
        String goldType = txnRequestDto.getGoldTypeName();
        Integer quantity = txnRequestDto.getQuantity();
        BigDecimal transactionPrice = txnRequestDto.getTransactionPrice();
        OffsetDateTime transactionDate = txnRequestDto.getTransactionDate();
        TransactionType transactionType = txnRequestDto.getTransactionType();

        // Check if the account exists
        if (!accountExists(accountId)) {
            LOG.warn("Unable to add transaction for account id: {}. Account does not exist", accountId);
            throw new IllegalStateException("Account for ID: " + accountId + " does not exist");
        }

        // Check if the gold type exists
        Integer goldTypeId = getGoldTypeId(goldType);
        if (goldTypeId == null) {
            LOG.warn("Unable to add transaction for gold type: {}. Gold type does not exist", goldType);
            throw new IllegalStateException("Gold type: " + goldType + " does not exist");
        }

        // Set quantity to 1 if it's null or zero
        int quantityValue = (quantity == null || quantity == 0) ? 1 : quantity;

        // Check if transaction price is null or non-positive
        if (transactionPrice == null || transactionPrice.compareTo(BigDecimal.ZERO) <= 0) {
            LOG.warn("Unable to process transaction. Transaction price is null or non-positive");
            throw new IllegalStateException("Invalid purchase price provided");
        }

        // Set transaction date to current date if it's null
        if (transactionDate == null) {
            LOG.warn("No purchase date provided. Assuming purchase was made today");
            transactionDate = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }

        // Create the transaction object
        Transaction transaction = new Transaction(accountId, goldTypeId, quantityValue, transactionType, transactionPrice, transactionDate);

        // Handle the transaction based on its type
        switch (transactionType) {
            case BUY:
                handleBuyTxn(transaction);
                break;
            case SELL:
                handleSellTxn(transaction);
                break;
            default:
                LOG.error("Unknown transaction type request. Unable to process");
                break;
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

    private void handleSellTxn(Transaction transaction) {
        //STUB METHOD
    }

    private void handleBuyTxn(Transaction transaction) {
        //STUB METHOD
    }
}
