package com.example.gatewayservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange
                        // CORS preflight
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Actuator abierto (ajusta si quieres protegerlo)
                        .pathMatchers("/actuator/**").permitAll()

                        // Swagger del GATEWAY (si algún día lo expones aquí mismo)
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()


                        .pathMatchers(
                                "/swipe-service/swagger-ui/**",
                                "/swipe-service/swagger-ui.html",
                                "/swipe-service/v3/api-docs/**"
                        ).permitAll()


                        // Todo lo demás requiere JWT válido
                        .anyExchange().authenticated()
                )

                // El gateway actúa como Resource Server (JWT)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }
}
