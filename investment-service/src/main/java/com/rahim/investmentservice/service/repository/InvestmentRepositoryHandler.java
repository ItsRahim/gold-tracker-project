package com.rahim.investmentservice.service.repository;

import com.rahim.investmentservice.entity.Investment;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public interface InvestmentRepositoryHandler {

    void saveInvestment(Investment investment);

    boolean investmentExists(int investmentId, int accountId);

    Investment getInvestmentById(int investmentId);

    void deleteInvestment(int investmentId);
}
