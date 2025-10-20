package com.example.gatewayservice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class HealthCheckConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckConfiguration.class);

    private final WebClient webClient;

    public HealthCheckConfiguration(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Agrega aquí los microservicios a monitorear.
     * En tu caso, usamos SOLO "swipe-service" y el ping que ya tienes:
     *   GET http://swipe-service/api/v1/swipes/ping
     */
    @Bean
    ReactiveHealthContributor healthcheckMicroservices() {
        Map<String, ReactiveHealthIndicator> registry = new LinkedHashMap<>();

        registry.put("swipe-service", () ->
                getHealth("http://swipe-service/api/v1/swipes/ping"));

        return CompositeReactiveHealthContributor.fromMap(registry);
    }

    private Mono<Health> getHealth(String url) {
        LOGGER.debug("Chequeando salud de: {}", url);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> Health.up().withDetail("url", url).withDetail("response", body).build())
                .onErrorResume(ex -> Mono.just(Health.down(ex).withDetail("url", url).build()))
                .log(LOGGER.getName(), Level.FINE);
    }
}
