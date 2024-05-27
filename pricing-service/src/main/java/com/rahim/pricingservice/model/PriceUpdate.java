package com.rahim.pricingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 27/05/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceUpdate {
    private Integer goldTypeId;
    private BigDecimal price;
}
