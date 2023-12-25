package com.rahim.notificationservice.model;

import com.rahim.notificationservice.listener.ThresholdAlertEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@EntityListeners(ThresholdAlertEntityListener.class)
@Table(name = "threshold_alerts", schema = "rgts")
public class ThresholdAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id", nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer user;

    @Column(name = "threshold_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal thresholdPrice;

    @Column(name = "is_active")
    private Boolean isActive;

    public void deactivate() {
        this.isActive = false;
    }
}