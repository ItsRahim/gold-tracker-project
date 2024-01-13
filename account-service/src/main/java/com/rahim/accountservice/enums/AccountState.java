package com.rahim.accountservice.enums;


import lombok.Getter;

@Getter
public enum AccountState {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    PENDING_DELETE("PENDING DELETE");

    private final String status;

    AccountState(String status) {
        this.status = status;
    }
}