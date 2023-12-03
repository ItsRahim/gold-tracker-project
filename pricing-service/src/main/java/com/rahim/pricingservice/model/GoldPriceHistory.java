package com.rahim.pricingservice.model;

import com.rahim.pricingservice.listener.GoldPriceHistoryEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "gold_price_history", schema = "rgts")
@EntityListeners(GoldPriceHistoryEntityListener.class)
public class GoldPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id", nullable = false)
    private Integer id;

    @Column(name = "open_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal openPrice;

    @Column(name = "close_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal closePrice;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

}