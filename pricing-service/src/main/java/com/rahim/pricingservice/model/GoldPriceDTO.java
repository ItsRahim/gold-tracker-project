package com.rahim.pricingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Rahim Ahmed
 * @created 03/12/2023
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoldPriceDTO {
    private Integer id;
    private String goldTypeName;
    private BigDecimal currentPrice;
    private Instant updatedAt;
}

