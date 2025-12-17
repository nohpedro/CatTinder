package com.example.gateway.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Expone las specs OpenAPI de cada microservicio a través del Gateway.
 * Esto permite que Swagger UI del gateway consuma /swagger/.../v3/api-docs
 */
@RestController
public class OpenApiProxyController {

    private final WebClient.Builder webClientBuilder;

    // inyección por constructor = sano, sin circular refs
    public OpenApiProxyController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping(
            value = "/swagger/users/v3/api-docs",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<String> usersApiDocs() {
        return webClientBuilder.build()
                .get()
                .uri("lb://USERS-SERVICE/v3/api-docs")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping(
            value = "/swagger/preferences/v3/api-docs",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<String> preferencesApiDocs() {
        return webClientBuilder.build()
                .get()
                .uri("lb://PREFERENCES-SERVICE/v3/api-docs")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping(
            value = "/swagger/matches/v3/api-docs",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<String> matchesApiDocs() {
        return webClientBuilder.build()
                .get()
                .uri("lb://MATCHES-SERVICE/v3/api-docs")
                .retrieve()
                .bodyToMono(String.class);
    }
}
