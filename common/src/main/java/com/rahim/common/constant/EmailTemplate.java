package com.rahim.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rahim Ahmed
 * @created 06/06/2024
 */
@Getter
@AllArgsConstructor
public enum EmailTemplate {
    ACCOUNT_DELETED("Account Deleted", "account-deleted.html", "Account Deletion Confirmation"),
    ACCOUNT_DELETION("Account Pending Deletion", "account-deletion.html", "Account Deletion Confirmation Required"),
    ACCOUNT_INACTIVITY("Account Inactivity", "account-inactivity.html", "Action Required: Reactivate Your Account"),
    ACCOUNT_UPDATE("Account Updated", "account-update.html", "Account Information Updated Successfully"),
    PRICE_ALERT("Price Alert", "price-alert.html", "Price Alert Notification");

    private final String templateDisplayName;
    private final String templateFileName;
    private final String subject;
}
