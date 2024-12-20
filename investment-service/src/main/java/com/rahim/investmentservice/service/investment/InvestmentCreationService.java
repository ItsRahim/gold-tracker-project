package com.rahim.investmentservice.service.investment;

import com.rahim.investmentservice.request.InvestmentRequest;

/**
 * @author Rahim Ahmed
 * @created 20/05/2024
 */
public interface InvestmentCreationService {

    void addNewInvestment(int accountId, InvestmentRequest investmentRequest);
}
