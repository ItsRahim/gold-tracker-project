package com.rahim.investmentservice.service.repository;

import com.rahim.investmentservice.model.Holding;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public interface HoldingRepositoryHandler {

    void saveHolding(Holding holding);
    void saveAllHoldings(List<Holding> holdings);
}
