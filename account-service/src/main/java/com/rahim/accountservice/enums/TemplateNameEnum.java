package com.rahim.accountservice.enums;

import lombok.Getter;

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

