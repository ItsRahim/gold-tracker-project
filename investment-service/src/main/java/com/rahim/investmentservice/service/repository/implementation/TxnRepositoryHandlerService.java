package com.rahim.investmentservice.service.repository.implementation;

import com.rahim.investmentservice.service.repository.TxnRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TxnRepositoryHandlerService implements TxnRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TxnRepositoryHandlerService.class);
    //private final TxnRepository txnRepository;
}
