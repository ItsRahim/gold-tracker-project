package com.rahim.accountservice.enums;

import lombok.Getter;

/**
 * This is an enumeration used to represent the various states of a user's account.
 * The states are:
 * - ACTIVE: The account is in a state where it can be used normally.
 * - INACTIVE: The account is not currently in use, but can be reactivated.
 * - PENDING_DELETE: The account is in a state where it is awaiting deletion.
 *
 * @author Rahim Ahmed
 * @created 04/11/2023
 */
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