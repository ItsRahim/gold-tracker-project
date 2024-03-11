package com.rahim.pricingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
    private OffsetDateTime updatedAt;
}

