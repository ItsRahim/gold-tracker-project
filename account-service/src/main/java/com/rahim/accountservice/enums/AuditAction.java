package com.rahim.accountservice.enums;


import lombok.Getter;

@Deprecated
@Getter
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