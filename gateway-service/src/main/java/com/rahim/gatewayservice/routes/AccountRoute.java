package com.rahim.gatewayservice.routes;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rahim.gatewayservice.constant.GatewayResponse.RESPONSE_HEADER_NAME;
import static com.rahim.gatewayservice.constant.GatewayResponse.RESPONSE_HEADER_VALUE;
import static com.rahim.gatewayservice.paths.ServiceEndpoint.USER_SERVICE_PREFIX;
import static com.rahim.gatewayservice.paths.ClientPath.*;
import static com.rahim.gatewayservice.paths.ClientPath.PROFILE_ROUTE;
import static com.rahim.gatewayservice.paths.ServiceHost.USER_SERVICE_URI;

/**
 * @author Rahim Ahmed
 * @created 22/04/2024
 */
@Configuration
public class AccountRoute {

    @Bean
    public RouteLocator accountServiceRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(this::defaultAccountRoute)
                .route(this::accountRoute)
                .route(this::defaultProfileRoute)
                .route(this::profileRoute)
                .build();
    }

    /**
     * ---------------------------------------------
     *  ACCOUNT MICROSERVICE ROUTES
     * ---------------------------------------------
     */
    private Buildable<Route> defaultAccountRoute(PredicateSpec r) {
        return r.path(DEFAULT_ACCOUNT_ROUTE)
                .filters(this::applyAccountFilters)
                .uri(USER_SERVICE_URI);
    }

    private Buildable<Route> accountRoute(PredicateSpec r) {
        return r.path(ACCOUNT_ROUTE)
                .filters(this::applyAccountFilters)
                .uri(USER_SERVICE_URI);
    }

    private Buildable<Route> defaultProfileRoute(PredicateSpec r) {
        return r.path(DEFAULT_PROFILE_ROUTE)
                .filters(this::applyAccountFilters)
                .uri(USER_SERVICE_URI);
    }

    private Buildable<Route> profileRoute(PredicateSpec r) {
        return r.path(PROFILE_ROUTE)
                .filters(this::applyAccountFilters)
                .uri(USER_SERVICE_URI);
    }

    private GatewayFilterSpec applyAccountFilters(GatewayFilterSpec f) {
        return f.prefixPath(USER_SERVICE_PREFIX)
                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE);
    }
}
