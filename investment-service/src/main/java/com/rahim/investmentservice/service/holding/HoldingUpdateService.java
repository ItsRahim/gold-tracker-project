package com.rahim.investmentservice.service.holding;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 27/05/2024
 */
public interface HoldingUpdateService {

    void updateCurrentValue(Integer goldTypeId, BigDecimal updatedPrice);
}
