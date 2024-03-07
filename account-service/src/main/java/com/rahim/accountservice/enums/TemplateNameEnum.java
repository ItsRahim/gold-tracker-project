package com.rahim.accountservice.enums;

import lombok.Getter;

/**
 * This enumeration contains names of email templates.
 * These templates are used by methods that trigger an email notification to a user about a specific event.
 * Each constant in this enum represents a unique email template.
 *
 * @author Rahim Ahmed
 * @created 25/02/2024
 */
@Getter
@Deprecated
public enum TemplateNameEnum {
    ACCOUNT_DELETED("Account Deleted"),
    ACCOUNT_DELETION("Account Deletion"),
    ACCOUNT_INACTIVITY("Account Inactivity"),
    ACCOUNT_UPDATE("Account Update");

    private final String templateName;

    TemplateNameEnum(String templateName) {
        this.templateName = templateName;
    }

    public static TemplateNameEnum fromTemplateName(String templateName) {
        for (TemplateNameEnum value : values()) {
            if (value.templateName.equals(templateName)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown template name: " + templateName);
    }
}

