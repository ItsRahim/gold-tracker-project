package com.rahim.investmentservice.constants;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public final class HoldingControllerEndpoint {

    private HoldingControllerEndpoint() {}

    public static final String BASE_URL = "/api/v1/holding";
    public static final String HOLDING_ID = "/{holdingId}";
    public static final String ACCOUNT_ID = "account/{accountId}";
}
