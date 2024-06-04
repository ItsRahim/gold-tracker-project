package com.rahim.pricingservice.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 01/12/2023
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GoldData {
    private String source;
    private BigDecimal price;
    private String requestDate;
}
