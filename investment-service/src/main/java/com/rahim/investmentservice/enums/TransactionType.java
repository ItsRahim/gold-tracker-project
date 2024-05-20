package com.rahim.investmentservice.enums;

import lombok.Getter;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Getter
public enum TransactionType {
    BUY("BUY"),
    SELL("SELL");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }
}

