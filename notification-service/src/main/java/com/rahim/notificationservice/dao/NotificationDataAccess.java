package com.rahim.notificationservice.dao;

/**
 * @author Rahim Ahmed
 * @created 27/04/2024
 */
public class NotificationDataAccess {

    private NotificationDataAccess() {}

    /**
     * ------------------------------------------
     * Threshold Alerts Table Constants
     * ------------------------------------------
     */
    public static final String TABLE_NAME = "rgts.threshold_alerts";
    public static final String COL_ALERT_ID = "alert_id";
    public static final String COL_ACCOUNT_ID = "account_id";
    public static final String COL_THRESHOLD_PRICE = "threshold_price";
    public static final String COL_IS_ACTIVE = "is_active";

    /**
     * ------------------------------------------
     * Account Table Constants
     * ------------------------------------------
     */
    public static final String ACCOUNT_TABLE_NAME = "rgts.user_accounts";
    public static final String ACCOUNT_COL_ACCOUNT_ID = "account_id";
    public static final String ACCOUNT_COL_EMAIL = "email";

    /**
     * ------------------------------------------
     * Profile Table Constants
     * ------------------------------------------
     */
    public static final String PROFILE_TABLE_NAME = "rgts.user_profiles";
    public static final String PROFILE_COL_FIRST_NAME = "first_name";
    public static final String PROFILE_COL_LAST_NAME = "last_name";
}
