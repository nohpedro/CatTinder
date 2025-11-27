package com.example.gatewayservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            // No usaremos sesiones ni formularios, solo JWT
            .csrf(ServerHttpSecurity.CsrfSpec::disable)

            .authorizeExchange(exchange -> exchange
                // CORS preflight
                .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                // Actuator abierto (ajusta si quieres protegerlo)
                .pathMatchers("/actuator/**").permitAll()

                // Si algún día expones Swagger propio del gateway:
                .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // Todo lo demás requiere JWT válido
                .anyExchange().authenticated()
            )

            // El gateway actúa como Resource Server (JWT)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }
}
