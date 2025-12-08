package com.example.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod() != null ? request.getMethod().name() : "-";
        String path = request.getURI().getRawPath();
        String query = request.getURI().getRawQuery();

        if (query != null && !query.isEmpty()) {
            log.info("GW -> {} {}?{}", method, path, query);
        } else {
            log.info("GW -> {} {}", method, path);
        }

        long startNanos = System.nanoTime();
        return chain.filter(exchange)
                .doOnError(throwable -> {
                    ServerHttpResponse response = exchange.getResponse();
                    int status = response.getStatusCode() != null ? response.getStatusCode().value() : 0;
                    long tookMs = (System.nanoTime() - startNanos) / 1_000_000;
                    log.info("GW <- {} {} status={} took={}ms error={}", method, path, status, tookMs, throwable.getClass().getSimpleName());
                })
                .doOnSuccess(unused -> {
                    ServerHttpResponse response = exchange.getResponse();
                    int status = response.getStatusCode() != null ? response.getStatusCode().value() : 0;
                    long tookMs = (System.nanoTime() - startNanos) / 1_000_000;
                    log.info("GW <- {} {} status={} took={}ms", method, path, status, tookMs);
                });
    }

    @Override
    public int getOrder() {
        // Ejecutar temprano, pero permitir que filtros de seguridad aún corran primero si existen
        return Ordered.LOWEST_PRECEDENCE - 100; // cercano al final para incluir status final
    }
}


