package com.example.swipeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Sin sesión, sin login por formulario, solo JWT
            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
                // Actuator (puedes restringirlo si quieres)
                .requestMatchers("/actuator/**").permitAll()

                // Swagger del micro
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/swagger-resources/**"
                ).permitAll()

                // TODO: si quieres dejar algún endpoint público, agrégalo aquí
                // .requestMatchers("/public/**").permitAll()

                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )

            // Resource Server con JWT
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                // Más adelante aquí podemos añadir un converter para roles de Keycloak
            }));

        return http.build();
    }
}
