package com.rahim.accountservice.model.request;

/**
 * @author Rahim Ahmed
 * @created 16/04/2024
 */
public class AccountRequestParam {

    private AccountRequestParam() {}

    public static final String ACCOUNT_EMAIL = "email";
    public static final String ACCOUNT_PASSWORD_HASH = "passwordHash";
    public static final String ACCOUNT_ACCOUNT_STATUS = "accountStatus";
    public static final String ACCOUNT_ACCOUNT_LOCKED = "accountLocked";
    public static final String ACCOUNT_CREDENTIALS_EXPIRED = "credentialsExpired";
    public static final String ACCOUNT_LAST_LOGIN = "lastLogin";
    public static final String ACCOUNT_NOTIFICATION_SETTING = "notificationSetting";
    public static final String ACCOUNT_CREATED_AT = "createdAt";
    public static final String ACCOUNT_UPDATED_AT = "updatedAt";
    public static final String ACCOUNT_LOGIN_ATTEMPTS = "loginAttempts";
    public static final String ACCOUNT_DELETE_DATE = "deleteDate";
}
