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
import static com.rahim.gatewayservice.paths.ClientPaths.*;
import static com.rahim.gatewayservice.paths.ServiceEndpoint.*;
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
                .route(this::defaultPriceRoute)
                .route(this::priceRoute)
                .route(this::defaultTypeRoute)
                .route(this::typeRoute)
                .build();
    }

    /*---------------------------------------------
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

    /*---------------------------------------------
     *  PRICING MICROSERVICE ROUTES
     * ---------------------------------------------
     */
    private Buildable<Route> defaultPriceRoute(PredicateSpec r) {
        return r.path(DEFAULT_PRICE_ROUTE)
                .filters(this::applyPriceFilters)
                .uri(PRICING_SERVICE_URI);
    }

    private Buildable<Route> priceRoute(PredicateSpec r) {
        return r.path(PRICE_ROUTE)
                .filters(this::applyPriceFilters)
                .uri(PRICING_SERVICE_URI);
    }

    private Buildable<Route> defaultTypeRoute(PredicateSpec r) {
        return r.path(DEFAULT_TYPE_ROUTE)
                .filters(this::applyPriceFilters)
                .uri(PRICING_SERVICE_URI);
    }

    private Buildable<Route> typeRoute(PredicateSpec r) {
        return r.path(TYPE_ROUTE)
                .filters(this::applyPriceFilters)
                .uri(PRICING_SERVICE_URI);
    }

    private GatewayFilterSpec applyPriceFilters(GatewayFilterSpec f) {
        return f.prefixPath(PRICING_SERVICE_PREFIX)
                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE)
                .rewritePath(DEFAULT_PRICE_ROUTE, INTERNAL_DEFAULT_PRICE_ROUTE)
                .rewritePath(PRICE_ROUTE + "(?<segment>.*)", INTERNAL_PRICE_ROUTE)
                .rewritePath(DEFAULT_TYPE_ROUTE, INTERNAL_DEFAULT_TYPE_ROUTE)
                .rewritePath(TYPE_ROUTE + "(?<segment>.*)", INTERNAL_TYPE_ROUTE);
    }
}

