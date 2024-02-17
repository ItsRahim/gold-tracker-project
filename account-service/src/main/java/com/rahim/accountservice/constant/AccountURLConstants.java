package com.rahim.accountservice.constant;

/**
 * This is a utility class that contains API endpoint constants related to Account operations.
 * It provides a centralized location for URLs, which can improve maintainability if the endpoints change.
 * As a final class, it cannot be extended, ensuring that all account-related URL constants are contained here.
 */
public final class AccountURLConstants {
    public static final String BASE_URL = "/api/v1/gold/user-service/account";
    public static final String ACCOUNT_ID = "/{accountId}";
}
