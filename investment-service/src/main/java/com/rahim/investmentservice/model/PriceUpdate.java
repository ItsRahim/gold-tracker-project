package com.rahim.investmentservice.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 29/05/2024
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PriceUpdate {
    private Integer holdingId;
    private Integer goldTypeId;
    private BigDecimal purchaseAmount;
    private BigDecimal currentValue;
}
