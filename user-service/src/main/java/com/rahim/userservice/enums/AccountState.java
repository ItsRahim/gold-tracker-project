package com.rahim.userservice.enums;


import lombok.Getter;

@Getter
public enum AccountStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    PENDING_DELETE("PENDING_DELETE");

    private final String status;

    AccountStatus(String status) {
        this.status = status;
    }
}