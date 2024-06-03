package com.rahim.investmentservice.service.transaction.implementation;

import com.rahim.investmentservice.entity.Transaction;
import com.rahim.investmentservice.repository.TxnRepository;
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
    private final TxnRepository txnRepository;

    @Override
    public void addNewTransaction(Transaction transaction) {
        txnRepository.save(transaction);
        LOG.debug("Transaction added successfully for account ID: {}", transaction.getAccountId());
    }
}
