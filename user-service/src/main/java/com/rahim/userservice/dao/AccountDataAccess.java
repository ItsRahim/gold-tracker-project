package com.rahim.userservice.dao;

/**
 * @author Rahim Ahmed
 * @created 23/04/2024
 */
public class AccountDataAccess {

    private AccountDataAccess() {}

    public static final String TABLE_NAME = "rgts.user_accounts";
    public static final String COL_ID = "account_id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_STATUS = "account_status";
    public static final String COL_LOCKED = "account_locked";
    public static final String COL_CREDENTIALS_EXPIRED = "credentials_expired";
    public static final String COL_LAST_LOGIN = "last_login";
    public static final String COL_NOTIFICATION_SETTING = "notification_setting";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";
    public static final String COL_LOGIN_ATTEMPTS = "login_attempts";
    public static final String COL_DELETE_DATE = "delete_date";

}
