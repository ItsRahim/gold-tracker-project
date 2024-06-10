package com.rahim.pricingservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Getter
@Setter
@Entity
@DynamicInsert
@NoArgsConstructor
@Table(name = "gold_prices", schema = "rgts")
public class GoldPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gold_type_id")
    @JsonProperty("name")
    private GoldType goldType;

    @Column(name = "current_price", nullable = false, precision = 10, scale = 2)
    @JsonProperty("currentPrice")
    private BigDecimal currentPrice;

    @Column(name = "updated_at")
    @JsonProperty("updatedAt")
    private Instant updatedAt;

    public GoldPrice(GoldType goldType, BigDecimal currentPrice) {
        this.goldType = goldType;
        this.currentPrice = currentPrice;
    }
}