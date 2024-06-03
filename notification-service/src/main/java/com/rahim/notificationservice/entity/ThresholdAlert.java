package com.rahim.notificationservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("alertId")
    private Integer id;

    @Column(name = "account_id")
    @JsonProperty("accountId")
    private Integer accountId;

    @Column(name = "threshold_price", nullable = false, precision = 10, scale = 2)
    @JsonProperty("thresholdPrice")
    private BigDecimal thresholdPrice;

    @JsonIgnore
    @Column(name = "is_active")
    @JsonProperty("isNotificationEnabled")
    private Boolean isActive;

    public void deactivate() {
        this.isActive = false;
    }
}