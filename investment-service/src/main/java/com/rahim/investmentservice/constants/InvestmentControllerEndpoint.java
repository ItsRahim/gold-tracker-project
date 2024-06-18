package com.rahim.investmentservice.constants;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
public final class InvestmentControllerEndpoint {

    private InvestmentControllerEndpoint() {}

    public static final String BASE_URL = "/api/v1/invest";
    public static final String ACCOUNT_ID = "/account/{accountId}";
    public static final String INVESTMENT_ID = "{investmentId}";
}
