package com.rahim.common.constant;

/**
 * @author Rahim Ahmed
 * @created 04/05/2024
 */
public class KafkaTopic {

    private KafkaTopic() {}

    /*Data required to fill email template placeholders are sent to this topic consumed by email-service*/
    public static final String SEND_ACCOUNT_ALERT = "email-account-alert";

    /*Topic produced by notification service. Consumed by email service to send price notification email*/
    public static final String SEND_PRICE_ALERT = "email-price-alert";

    /*Topic produced by scheduler-service and consumed by account-service used to trigger an account cleanup job*/
    public static final String ACCOUNT_CLEANUP = "account-cleanup";

    /*Topic produced by scheduler-service consumed by pricing-service used to trigger a price update job in pricing-service*/
    public static final String PRICE_UPDATE = "price-update";

    /*Topic produced by scheduler-service & consumed by pricing-service used to trigger EoD to update to price history table*/
    public static final String PRICE_HISTORY_UPDATE = "price-history-update";

    /*Topic produced by pricing-service & consumed by notification-service to check if price notifications have been met*/
    public static final String THRESHOLD_PRICE_UPDATE = "threshold-price-update";

    /*Topic produced by pricing-service and consumed by investment-service to update current value and profit loss for holdings*/
    public static final String HOLDING_PRICE_UPDATE = "holding-price-update";
}
