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
import static com.rahim.gatewayservice.paths.ServiceEndpoint.PRICING_SERVICE_PREFIX;
import static com.rahim.gatewayservice.paths.ClientPath.*;
import static com.rahim.gatewayservice.paths.ClientPath.TYPE_ROUTE;
import static com.rahim.gatewayservice.paths.ServiceHost.PRICING_SERVICE_URI;
import static com.rahim.gatewayservice.paths.ServicePath.*;
import static com.rahim.gatewayservice.paths.ServicePath.INTERNAL_TYPE_ROUTE;

/**
 * @author Rahim Ahmed
 * @created 22/04/2024
 */
@Configuration
public class PricingRoute {

    @Bean
    public RouteLocator pricingServiceRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(this::defaultPriceRoute)
                .route(this::priceRoute)
                .route(this::defaultTypeRoute)
                .route(this::typeRoute)
                .build();
    }

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
