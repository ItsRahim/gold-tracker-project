package com.rahim.pricingservice.dao;

/**
 * @author Rahim Ahmed
 * @created 29/04/2024
 */
public class GoldPriceDataAccess {

    private GoldPriceDataAccess() {}

    public static final String TABLE_NAME = "rgts.gold_prices";
    public static final String COL_GOLD_PRICE_ID = "price_id";
    public static final String COL_GOLD_TYPE_ID = "gold_type_id";
    public static final String COL_CURRENT_PRICE = "current_price";
    public static final String COL_UPDATED_AT = "updated_at";
}
