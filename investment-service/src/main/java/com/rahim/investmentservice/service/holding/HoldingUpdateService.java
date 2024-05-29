package com.rahim.investmentservice.service.holding;

/**
 * @author Rahim Ahmed
 * @created 27/05/2024
 */
public interface HoldingUpdateService {

    void updateCurrentValue(Integer goldTypeId, Double updatedPrice);
}
