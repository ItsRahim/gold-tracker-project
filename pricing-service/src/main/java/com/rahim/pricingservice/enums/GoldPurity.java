package com.rahim.pricingservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Getter
@AllArgsConstructor
public enum GoldPurity {

    K24(24.0 / 24, "24K"),
    K23(23.0 / 24, "23K"),
    K22(22.0 / 24, "22K"),
    K21(21.0 / 24, "21K"),
    K20(20.0 / 24, "20K"),
    K19(19.0 / 24, "19K"),
    K18(18.0 / 24, "18K"),
    K17(17.0 / 24, "17K"),
    K16(16.0 / 24, "16K"),
    K15(15.0 / 24, "15K"),
    K14(14.0 / 24, "14K"),
    K13(13.0 / 24, "13K"),
    K12(12.0 / 24, "12K"),
    K11(11.0 / 24, "11K"),
    K10(10.0 / 24, "10K"),
    K9(9.0 / 24, "9K"),
    K8(8.0 / 24, "8K"),
    K7(7.0 / 24, "7K"),
    K6(6.0 / 24, "6K"),
    K5(5.0 / 24, "5K"),
    K4(4.0 / 24, "4K"),
    K3(3.0 / 24, "3K"),
    K2(2.0 / 24, "2K"),
    K1(1.0 / 24, "1K");

    private final double purity;
    private final String carat;

    public static double getPurityByCarat(String carat) {
        for (GoldPurity goldPurity : GoldPurity.values()) {
            if (goldPurity.getCarat().equalsIgnoreCase(carat)) {
                return goldPurity.getPurity();
            }
        }

        throw new IllegalArgumentException("Invalid gold purity label: " + carat);
    }

    public static boolean existsByCarat(String carat) {
        for (GoldPurity goldPurity : GoldPurity.values()) {
            if (goldPurity.getCarat().equalsIgnoreCase(carat)) {
                return true;
            }
        }
        return false;
    }
}
