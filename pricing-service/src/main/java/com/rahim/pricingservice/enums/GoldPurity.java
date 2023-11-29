package com.rahim.pricingservice.enums;

import lombok.Getter;

@Getter
public enum GoldPurity {
    K24(24.0 / 24, "24K"),
    K22(22.0 / 24, "22K"),
    K18(18.0 / 24, "18K"),
    K14(14.0 / 24, "14K"),
    K12(12.0 / 24, "12K"),
    K10(10.0 / 24, "10K"),
    K9(9.0 / 24, "9K"),
    K8(8.0 / 24, "8K"),
    K6(6.0 / 24, "6K");

    private final double purity;
    private final String label;

    GoldPurity(double purity, String label) {
        this.purity = purity;
        this.label = label;
    }
}
