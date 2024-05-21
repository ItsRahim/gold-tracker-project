package com.rahim.investmentservice.service.holding;

import com.rahim.investmentservice.model.Holding;

/**
 * @author Rahim Ahmed
 * @created 21/05/2024
 */
public interface HoldingCreationService {

    void processNewHolding(Holding holding, int goldTypeId, int quantity);
}
