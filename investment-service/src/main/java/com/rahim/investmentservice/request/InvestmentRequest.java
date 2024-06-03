package com.rahim.investmentservice.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Getter
public class InvestmentRequest {
    private String goldTypeName;
    private Integer quantity;
    private BigDecimal totalPurchasePrice;
    private LocalDate purchaseDate;
}
