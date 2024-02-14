package com.rahim.batchimport.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GoldPriceHistory {
    private Integer id;
    private BigDecimal priceOunce;
    private BigDecimal priceGram;
    private LocalDate effectiveDate;

    public GoldPriceHistory(BigDecimal priceOunce, BigDecimal priceGram, LocalDate effectiveDate) {
        this.priceOunce = priceOunce;
        this.priceGram = priceGram;
        this.effectiveDate = effectiveDate;
    }
}
