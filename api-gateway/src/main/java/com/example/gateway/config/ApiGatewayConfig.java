package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                // =====================================================
                // RUTAS DE NEGOCIO
                // =====================================================

                // USERS API -> /users/** -> USERS-SERVICE
                .route("users-service-api", r -> r
                        .path("/users/**")
                        .filters(f -> f.stripPrefix(1)) // /users/... -> /...
                        .uri("lb://USERS-SERVICE")
                )

                // PREFERENCES API -> /preferences/** -> PREFERENCES-SERVICE
                .route("preferences-service-api", r -> r
                        .path("/preferences/**")
                        .filters(f -> f.stripPrefix(1)) // /preferences/... -> /...
                        .uri("lb://PREFERENCES-SERVICE")
                )

                // MATCHES API -> /matches/** -> MATCHES-SERVICE
                .route("matches-service-api", r -> r
                        .path("/matches/**")
                        .filters(f -> f.stripPrefix(1)) // /matches/... -> /...
                        .uri("lb://MATCHES-SERVICE")
                )

                // =====================================================
                // SWAGGER USERS VÍA GATEWAY
                // =====================================================
                .route("users-swagger", r -> r
                        .path("/swagger/users/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://USERS-SERVICE")
                )

                // =====================================================
                // SWAGGER PREFERENCES VÍA GATEWAY
                // =====================================================
                .route("preferences-swagger", r -> r
                        .path("/swagger/preferences/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://PREFERENCES-SERVICE")
                )

                // =====================================================
                // SWAGGER MATCHES VÍA GATEWAY
                // =====================================================
                .route("matches-swagger", r -> r
                        .path("/swagger/matches/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://MATCHES-SERVICE")
                )

                .build();
    }
}
