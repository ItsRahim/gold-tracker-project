package com.rahim.investmentservice.service.repository;

import com.rahim.investmentservice.model.Investment;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public interface InvestmentRepositoryHandler {

    void saveInvestment(Investment investment);

    boolean investmentExists(int accountId, int investmentId);

    Investment getInvestmentById(int investmentId);
}
