package com.rahim.investmentservice.service.holding.implementation;

import com.rahim.investmentservice.service.holding.HoldingDeletionService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 23/05/2024
 */
@Service
@RequiredArgsConstructor
public class HoldingDeletionImpl implements HoldingDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingCreationImpl.class);
    private final HoldingRepositoryHandler holdingRepositoryHandler;

    @Override
    public void sellHolding(int holdingId) {

    }
}
