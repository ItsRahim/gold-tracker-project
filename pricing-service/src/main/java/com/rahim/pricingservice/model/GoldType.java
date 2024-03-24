package com.rahim.pricingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rahim.pricingservice.listener.GoldTypeEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Getter
@Setter
@Entity
@Table(name = "gold_types", schema = "rgts")
@EntityListeners(GoldTypeEntityListener.class)
public class GoldType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gold_type_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    @JsonProperty("name")
    private String name;

    @Column(name = "net_weight", nullable = false, precision = 5, scale = 2)
    @JsonProperty("netWeight")
    private BigDecimal netWeight;

    @Column(name = "carat", nullable = false, length = 3)
    @JsonProperty("carat")
    private String carat;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    @JsonProperty("description")
    private String description;

}