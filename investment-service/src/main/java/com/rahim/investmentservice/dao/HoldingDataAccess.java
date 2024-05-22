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
    public static final String COL_INVESTMENT_ID = "investment_id";
    public static final String COL_TOTAL_PURCHASE_AMOUNT = "total_purchase_amount";
    public static final String COL_CURRENT_VALUE = "current_value";
    public static final String COL_PROFIT_LOSS = "profile_loss";
}
