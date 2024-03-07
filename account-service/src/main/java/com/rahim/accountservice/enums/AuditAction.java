package com.rahim.accountservice.enums;

import lombok.Getter;

/**
 * @author Rahim Ahmed
 * @created 25/02/2024
 */
@Getter
@Deprecated
public enum AuditAction {
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE_REQUEST("DELETE REQUEST"),
    DELETE("DELETE");

    private final String action;

    AuditAction(String action) {
        this.action = action;
    }
}