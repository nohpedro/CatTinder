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

                // USERS API -> http://localhost:8083/users/api/v1/...
                .route("users-service-api", r -> r
                        .path("/users/**")
                        .filters(f -> f.stripPrefix(1)) // /users/... -> /...
                        .uri("lb://USERS-SERVICE")
                )

                // PREFERENCES API -> http://localhost:8083/preferences/api/v1/...
                .route("preferences-service-api", r -> r
                        .path("/preferences/**")
                        .filters(f -> f.stripPrefix(1)) // /preferences/... -> /...
                        .uri("lb://PREFERENCES-SERVICE")
                )

                // =====================================================
                // SWAGGER USERS VÍA GATEWAY
                // =====================================================
                //
                // Ejemplo:
                //   /swagger/users/swagger-ui/index.html
                //   /swagger/users/swagger-ui/swagger-initializer.js
                //   /swagger/users/v3/api-docs
                //
                // stripPrefix(2):
                //   /swagger/users/swagger-ui/index.html  -> /swagger-ui/index.html
                //   /swagger/users/v3/api-docs           -> /v3/api-docs
                //

                .route("users-swagger", r -> r
                        .path("/swagger/users/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://USERS-SERVICE")
                )

                // =====================================================
                // SWAGGER PREFERENCES VÍA GATEWAY
                // =====================================================
                //
                //   /swagger/preferences/swagger-ui/index.html
                //   /swagger/preferences/v3/api-docs
                //
                .route("preferences-swagger", r -> r
                        .path("/swagger/preferences/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://PREFERENCES-SERVICE")
                )

                .build();
    }
}
