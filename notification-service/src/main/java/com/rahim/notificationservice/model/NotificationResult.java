package com.rahim.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 27/04/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResult {
    private Integer alertId;
    private String firstName;
    private String lastName;
    private String email;
    private double thresholdPrice;
    private boolean isActive;

    public NotificationResult(Integer alertId, String firstName, String lastName, String email, double thresholdPrice) {
        this.alertId = alertId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.thresholdPrice = thresholdPrice;
    }
}
