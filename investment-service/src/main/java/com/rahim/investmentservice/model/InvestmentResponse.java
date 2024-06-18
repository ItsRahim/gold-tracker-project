package com.rahim.investmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 18/06/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentResponse {
    private BigDecimal profitLoss;
    private BigDecimal currentValue;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
}
