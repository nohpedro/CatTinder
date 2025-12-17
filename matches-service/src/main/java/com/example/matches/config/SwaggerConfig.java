package com.example.matches.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI matchesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Matches")
                        .description("Gestión de swipes, matches y bloqueos")
                        .version("1.0"));
    }
}
