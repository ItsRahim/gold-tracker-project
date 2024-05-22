package com.rahim.investmentservice.dao;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public class TransactionDataAccess {

    private TransactionDataAccess() {}

    public static final String TABLE_NAME = "rgts.transactions";
    public static final String COL_TRANSACTION_ID = "transaction_id";
    public static final String COL_ACCOUNT_ID = "account_id";
    public static final String COL_GOLD_TYPE_ID = "gold_type_id";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_TRANSACTION_TYPE = "transaction_type";
    public static final String COL_TRANSACTION_PRICE = "transaction_price";
    public static final String COL_TRANSACTION_DATE = "transaction_date";
}
