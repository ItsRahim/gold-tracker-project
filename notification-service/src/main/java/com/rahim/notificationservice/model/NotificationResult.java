package com.rahim.notificationservice.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 27/04/2024
 */
@Getter
@Setter
public class NotificationResult {

    private Integer alertId;
    private String firstName;
    private String lastName;
    private String email;
    private double thresholdPrice;
    private boolean isActive;
}
