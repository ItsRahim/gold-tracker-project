package com.rahim.accountservice.constant;

/**
 * This is a utility class that contains API endpoint constants related to Profile operations.
 * It provides a centralized location for URLs, which can improve maintainability if the endpoints change.
 * As a final class, it cannot be extended, ensuring that all profile-related URL constants are contained here.
 */
public final class ProfileURLConstants {
    public static final String BASE_URL = "/api/v1/gold/user-service/profile";
    public static final String PROFILE_ID= "/{profileId}";
    public static final String USERNAME = "/{username}";
}
