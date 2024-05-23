package com.rahim.investmentservice.service.investment;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 21/05/2024
 */
public interface InvestmentDeletionService {

    void sellInvestment(int investmentId, int accountId, BigDecimal transactionAmount, BigDecimal purchaseAmount);
}
