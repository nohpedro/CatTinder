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

                // RUTA USERS
                .route("users-service", r -> r
                        // cuando el cliente pega a /users/** en el gateway...
                        .path("/users/**")
                        // ...le quitamos el primer segmento (/users)
                        .filters(f -> f.stripPrefix(1))
                        // ...y lo mandamos al servicio registrado en Eureka
                        .uri("lb://USERS-SERVICE")
                )

                // RUTA PREFERENCES (para que ya lo tengas listo)
                .route("preferences-service", r -> r
                        .path("/preferences/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://PREFERENCES-SERVICE")
                )

                .build();
    }
}
