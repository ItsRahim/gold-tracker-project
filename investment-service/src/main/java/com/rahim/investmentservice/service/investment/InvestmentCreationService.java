package com.rahim.investmentservice.service.investment;

import com.rahim.investmentservice.dto.HoldingRequestDto;

/**
 * @author Rahim Ahmed
 * @created 20/05/2024
 */
public interface InvestmentCreationService {

    void addNewHolding(int accountId, HoldingRequestDto holdingRequestDto);
}
