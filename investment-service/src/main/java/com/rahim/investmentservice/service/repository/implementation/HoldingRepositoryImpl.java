package com.rahim.investmentservice.service.repository.implementation;

import com.rahim.investmentservice.repository.HoldingRepository;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
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
public class HoldingRepositoryImpl implements HoldingRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingRepositoryImpl.class);
    //private final HoldingRepository holdingRepository;
}
