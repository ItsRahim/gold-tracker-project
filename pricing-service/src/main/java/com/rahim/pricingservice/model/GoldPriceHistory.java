package com.rahim.pricingservice.model;

import com.rahim.pricingservice.listener.GoldPriceHistoryEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 01/12/2023
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "gold_price_history", schema = "rgts")
@EntityListeners(GoldPriceHistoryEntityListener.class)
public class GoldPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id", nullable = false)
    private Integer id;

    @Column(name = "price_ounce", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceOunce;

    @Column(name = "price_gram", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceGram;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    public GoldPriceHistory(BigDecimal priceOunce, BigDecimal priceGram, LocalDate effectiveDate) {
        this.priceOunce = priceOunce;
        this.priceGram = priceGram;
        this.effectiveDate = effectiveDate;
    }
}