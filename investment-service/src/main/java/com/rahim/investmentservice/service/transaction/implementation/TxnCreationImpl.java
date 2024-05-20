package com.rahim.investmentservice.service.transaction.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.investmentservice.dto.TxnRequestDto;
import com.rahim.investmentservice.model.Transaction;
import com.rahim.investmentservice.service.repository.TxnRepositoryHandler;
import com.rahim.investmentservice.service.transaction.TxnCreationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Service
@RequiredArgsConstructor
public class TxnCreationImpl implements TxnCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(TxnCreationImpl.class);
    private final TxnRepositoryHandler txnRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    @Override
    public void addNewTransaction(Transaction transaction) {

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
