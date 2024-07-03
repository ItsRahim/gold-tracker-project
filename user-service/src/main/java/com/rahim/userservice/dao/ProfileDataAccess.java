package com.rahim.userservice.dao;

/**
 * @author Rahim Ahmed
 * @created 23/04/2024
 *
 * Utility class which contains references to columns in rgts.user_profiles table
 */
public class ProfileDataAccess {

    private ProfileDataAccess() {
    }

    public static final String TABLE_NAME = "rgts.user_profiles";

    public static final String COL_PROFILE_ID = "profile_id";

    public static final String COL_ACCOUNT_ID = "account_id";

    public static final String COL_PROFILE_USERNAME = "username";

    public static final String COL_PROFILE_FIRST_NAME = "first_name";

    public static final String COL_PROFILE_LAST_NAME = "last_name";

    public static final String COL_PROFILE_CONTACT_NUMBER = "contact_number";

    public static final String COL_PROFILE_ADDRESS = "address";
}
