package com.rahim.notificationservice.enums;

import lombok.Getter;

@Getter
public enum TemplateNameEnum {
    PRICE_ALERT("Gold Price Alert");

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

