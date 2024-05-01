package com.rahim.notificationservice.constants;

/**
 * This is a utility class that contains API endpoint constants related to Account operations.
 *
 * @author Rahim Ahmed
 * @created 16/02/2024
 */
public final class ThresholdControllerEndpoint {

    private ThresholdControllerEndpoint(){}

    public static final String BASE_URL = "/api/v1/gold/notification-service";
    public static final String THRESHOLD_ID = "/{thresholdId}";
    public static final String ACCOUNT_ID = "/{accountId}";
}
