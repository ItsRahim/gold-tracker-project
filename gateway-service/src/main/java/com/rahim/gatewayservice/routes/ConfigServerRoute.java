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
import static com.rahim.gatewayservice.paths.ServiceEndpoint.CONFIG_SERVER_PREFIX;
import static com.rahim.gatewayservice.paths.ClientPath.JAVA_ENCRYPTION;
import static com.rahim.gatewayservice.paths.ClientPath.PYTHON_ENCRYPTION;
import static com.rahim.gatewayservice.paths.ServiceHost.CONFIG_SERVER_URI;

/**
 * @author Rahim Ahmed
 * @created 22/04/2024
 */
@Configuration
public class ConfigServerRoute {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(this::javaEncryptionRoute)
                .route(this::pythonEncryptionRoute)
                .build();
    }

    /**
     * ---------------------------------------------
     *  CONFIG SERVER ENCRYPTION ROUTES
     * ---------------------------------------------
     */
    private Buildable<Route> javaEncryptionRoute(PredicateSpec r) {
        return r.path(JAVA_ENCRYPTION)
                .filters(this::applyConfigFilter)
                .uri(CONFIG_SERVER_URI);
    }

    private Buildable<Route> pythonEncryptionRoute(PredicateSpec r) {
        return r.path(PYTHON_ENCRYPTION)
                .filters(this::applyConfigFilter)
                .uri(CONFIG_SERVER_URI);
    }

    private GatewayFilterSpec applyConfigFilter(GatewayFilterSpec f) {
        return f.prefixPath(CONFIG_SERVER_PREFIX)
                .addResponseHeader(RESPONSE_HEADER_NAME, RESPONSE_HEADER_VALUE);
    }
}
