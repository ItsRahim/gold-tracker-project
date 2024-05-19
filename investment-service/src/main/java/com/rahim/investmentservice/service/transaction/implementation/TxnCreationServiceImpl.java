package com.rahim.investmentservice.service.transaction.implementation;

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

    @Override
    public void addNewTransaction(TxnRequestDto txnRequestDto) {

    }
}
