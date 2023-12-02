package com.rahim.pricingservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "gold_prices", schema = "rgts")
public class GoldPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gold_type_id")
    private GoldType goldType;

    @Column(name = "current_price", nullable = false, precision = 5, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}