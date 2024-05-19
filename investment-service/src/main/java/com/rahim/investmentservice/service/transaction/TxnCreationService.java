package com.rahim.investmentservice.service.transaction;

import com.rahim.investmentservice.dto.TxnRequestDto;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public interface TxnCreationService {

    void addNewTransaction(int accountId, TxnRequestDto txnRequestDto);
}
