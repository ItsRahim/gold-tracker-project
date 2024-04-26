package com.rahim.gatewayservice.paths;

/**
 * @author Rahim Ahmed
 * @created 13/04/2024
 */
public final class ClientPath {

    private ClientPath() {}

    /**
     *  ---------------------------------------------
     *  CONFIG SERVER CLIENT ROUTES
     *  ---------------------------------------------
     */
    public static final String JAVA_ENCRYPTION = "/encrypt";
    public static final String PYTHON_ENCRYPTION = JAVA_ENCRYPTION + "/python";

    /**
     *  ---------------------------------------------
     *  ACCOUNT MICROSERVICE CLIENT ROUTES
     *  ---------------------------------------------
     */
    public static final String DEFAULT_ACCOUNT_ROUTE = "/account";
    public static final String ACCOUNT_ROUTE = "/account/**";
    public static final String DEFAULT_PROFILE_ROUTE = "/profile";
    public static final String PROFILE_ROUTE = "/profile/**";

    /**
     *  ---------------------------------------------
     *  PRICING MICROSERVICE CLIENT ROUTES
     *  ---------------------------------------------
     */
    public static final String DEFAULT_PRICE_ROUTE = "/price";
    public static final String PRICE_ROUTE = "/price/*";
    public static final String DEFAULT_TYPE_ROUTE = "/type";
    public static final String TYPE_ROUTE = "/type/*";
}
