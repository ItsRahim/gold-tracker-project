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
    ACCOUNT_DELETED("account-deleted.html", "Account Deleted", "Account Deletion Confirmation"),
    ACCOUNT_DELETION("account-deletion.html", "Account Pending Deletion", "Account Deletion Confirmation Required"),
    ACCOUNT_INACTIVITY("account-inactivity.html", "Account Inactivity", "Action Required: Reactivate Your Account"),
    ACCOUNT_UPDATE("account-update.html", "Account Updated", "Account Information Updated Successfully"),
    PRICE_ALERT("price-alert.html", "Price Alert", "Price Alert Notification");

    private final String templateFileName;
    private final String templateDisplayName;
    private final String subject;
}
