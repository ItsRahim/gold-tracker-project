package com.rahim.pricingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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

    @Column(name = "current_price", nullable = false, precision = 5, scale = 2)
    @JsonProperty("currentPrice")
    private BigDecimal currentPrice;

    @Column(name = "updated_at")
    @JsonProperty("updatedAt")
    private OffsetDateTime updatedAt;

    public GoldPrice(GoldType goldType, BigDecimal currentPrice) {
        this.goldType = goldType;
        this.currentPrice = currentPrice;
        this.updatedAt = OffsetDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
    }
}