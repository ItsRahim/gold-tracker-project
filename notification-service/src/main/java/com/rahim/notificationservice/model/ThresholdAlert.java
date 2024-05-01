package com.rahim.notificationservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@DynamicInsert
@RequiredArgsConstructor
@Table(name = "threshold_alerts", schema = "rgts")
public class ThresholdAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id", nullable = false)
    private Integer id;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "threshold_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal thresholdPrice;

    @Column(name = "is_active")
    private Boolean isActive;

    public void deactivate() {
        this.isActive = false;
    }
}