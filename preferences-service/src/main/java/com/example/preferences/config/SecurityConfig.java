package com.example.preferences.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (Swagger)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                // OAuth2 Resource Server con JWT usando el estilo moderno
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                // CSRF desactivado para API REST
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
