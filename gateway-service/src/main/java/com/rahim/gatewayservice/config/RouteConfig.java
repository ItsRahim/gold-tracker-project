package com.rahim.gatewayservice.config;

/**
 * @author Rahim Ahmed
 * @created 13/04/2024
 */
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rahim.gatewayservice.paths.PrefixPaths.*;
import static com.rahim.gatewayservice.paths.UriPaths.*;

@Configuration
public class RouteConfig {

    private static final String RESPONSE_HEADER_NAME = "X-Powered-By";
    private static final String RESPONSE_HEADER_VALUE = "Rahim Gateway Service";

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("account-route", r -> r
                        .path("/account/**")
                        .filters(f -> f.prefixPath(USER_SERVICE_PREFIX)
                                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE))
                        .uri(USER_SERVICE_URI))
                .route("profile-route", r -> r
                        .path("/profile/**")
                        .filters(f -> f.prefixPath(USER_SERVICE_PREFIX)
                                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE))
                        .uri(USER_SERVICE_URI))
                .route("price-route", r -> r
                        .path("/gold-price/**")
                        .filters(f -> f.prefixPath(PRICING_SERVICE_PREFIX)
                                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE))
                        .uri(PRICING_SERVICE_URI))
                .route("type-route", r -> r
                        .path("/gold-type/**")
                        .filters(f -> f.prefixPath(PRICING_SERVICE_PREFIX)
                                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE))
                        .uri(PRICING_SERVICE_URI))
                .build();
    }
}

