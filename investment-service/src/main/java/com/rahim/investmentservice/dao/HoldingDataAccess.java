package com.rahim.investmentservice.dao;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public class HoldingDataAccess {

    private HoldingDataAccess() {}

    public static final String TABLE_NAME = "rgts.holdings";
    public static final String COL_HOLDING_ID = "holding_id";
    public static final String COL_ACCOUNT_ID = "account_id";
    public static final String COL_GOLD_TYPE_ID = "gold_type_id";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_PURCHASE_PRICE = "purchase_price";
    public static final String COL_PURCHASE_DATA = "purchase_date";
}
