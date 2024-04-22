package com.rahim.gatewayservice.paths;

/**
 * @author Rahim Ahmed
 * @created 13/04/2024
 */
public final class ServiceHost {

    private ServiceHost() {}

    public static final String CONFIG_SERVER_URI = "http://localhost:8888";
    public static final String USER_SERVICE_URI = "http://localhost:8081";
    public static final String PRICING_SERVICE_URI = "http://localhost:8084";
}
