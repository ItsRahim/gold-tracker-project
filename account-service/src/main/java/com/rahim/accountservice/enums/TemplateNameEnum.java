package com.rahim.accountservice.enums;

import lombok.Getter;

/**
 * This enumeration contains names of email templates.
 * It provides a type-safe way to define and use constants that represent different email templates.
 * The @Getter annotation from the Lombok library generates getters for all fields.
 * <p>
 * These templates are used by methods that trigger an email notification to a user about a specific event.
 * Each constant in this enum represents a unique email template.
 */
@Getter
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

