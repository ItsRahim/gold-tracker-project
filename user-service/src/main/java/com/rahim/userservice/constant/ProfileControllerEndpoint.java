package com.rahim.userservice.constant;

/**
 * This is a utility class that contains API endpoint constants related to Profile operations.
 *
 * @author Rahim Ahmed
 * @created 16/02/2024
 */
public final class ProfileControllerEndpoint {

    private ProfileControllerEndpoint() {
    }

    public static final String BASE_URL = "/api/v1/profile";

    public static final String PROFILE_ID = "/{profileId}";

    public static final String USERNAME = "/{username}";
}
