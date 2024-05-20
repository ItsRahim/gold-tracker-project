package com.rahim.investmentservice.service.transaction;

import com.rahim.investmentservice.model.Transaction;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public interface TxnCreationService {

    void addNewTransaction(Transaction transaction);
}
