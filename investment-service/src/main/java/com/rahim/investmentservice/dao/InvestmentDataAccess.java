package com.rahim.investmentservice.dao;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public class InvestmentDataAccess {

    private InvestmentDataAccess() {}

    public static final String TABLE_NAME = "rgts.investments";
    public static final String COL_INVESTMENT_ID = "investment_id";
    public static final String COL_ACCOUNT_ID = "account_id";
    public static final String COL_TOTAL_PURCHASE_AMOUNT = "total_purchase_amount";
    public static final String COL_CURRENT_VALUE = "current_value";
    public static final String COL_PROFIT_LOSS = "profile_loss";
    public static final String COL_TOTAL_WEIGHT = "total_weight";
}
