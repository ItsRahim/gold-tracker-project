package com.rahim.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rahim.gatewayservice.paths.ApiPaths.*;
import static com.rahim.gatewayservice.paths.ServicePath.*;

/**
 * @author Rahim Ahmed
 * @created 13/04/2024
 */
@Configuration
public class RouteConfig {

    private static final String RESPONSE_HEADER_NAME = "X-Powered-By";
    private static final String RESPONSE_HEADER_VALUE = "Rahim Gateway Service";

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(this::defaultAccountRoute)
                .route(this::accountRoute)
                .route(this::defaultProfileRoute)
                .route(this::profileRoute)
                .build();
    }

    /*---------------------------------------------
    *  ACCOUNT SERVICE ROUTES
    * ---------------------------------------------
    */
    private Buildable<Route> defaultAccountRoute(PredicateSpec r) {
        return r.path("/account")
                .filters(this::applyFilters)
                .uri(USER_SERVICE_URI);
    }

    private Buildable<Route> accountRoute(PredicateSpec r) {
        return r.path("/account/**")
                .filters(this::applyFilters)
                .uri(USER_SERVICE_URI);
    }

    private Buildable<Route> defaultProfileRoute(PredicateSpec r) {
        return r.path("/profile")
                .filters(this::applyFilters)
                .uri(USER_SERVICE_URI);
    }

    private Buildable<Route> profileRoute(PredicateSpec r) {
        return r.path("/profile/**")
                .filters(this::applyFilters)
                .uri(USER_SERVICE_URI);
    }

    private GatewayFilterSpec applyFilters(GatewayFilterSpec f) {
        return f.prefixPath(USER_SERVICE_PREFIX)
                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE);
    }

}
