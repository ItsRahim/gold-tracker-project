package com.rahim.investmentservice.service.transaction.implementation;

import com.hazelcast.collection.ISet;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.investmentservice.dto.TxnRequestDto;
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
public class TxnCreationServiceImpl implements TxnCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(TxnCreationServiceImpl.class);
    private final TxnRepositoryHandler txnRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    @Override
    public void addNewTransaction(int accountId, TxnRequestDto txnRequestDto) {
        if (!accountExists(accountId)) {
            LOG.warn("Unable to add transaction for account id: {}. Account does not exist", accountId);
        }
    }

    private boolean accountExists(int accountId) {
        ISet<Integer> accountIds = hazelcastCacheManager.getSet(HazelcastConstant.);

        return accountIds.contains(accountId);
    }
}
