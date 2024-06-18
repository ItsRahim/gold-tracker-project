package com.rahim.investmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 18/06/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoldingResponse {
    private BigDecimal purchaseAmount;
    private BigDecimal currentValue;
    private BigDecimal profitLoss;
}
