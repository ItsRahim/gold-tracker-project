package com.rahim.investmentservice.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Getter
public class HoldingRequestDto {

    private String goldTypeName;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
}
