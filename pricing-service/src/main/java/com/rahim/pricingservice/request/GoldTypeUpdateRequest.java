package com.rahim.pricingservice.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 03/06/2024
 */
@Getter
@Setter
public class GoldTypeUpdateRequest {
    private String name;
    private BigDecimal netWeight;
    private String carat;
    private String description;
}
