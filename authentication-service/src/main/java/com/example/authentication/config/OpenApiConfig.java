package com.example.authentication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CatTinder Authentication Service")
                        .version("1.0")
                        .description("Microservicio de autenticación para CatTinder")
                        .contact(new Contact()
                                .name("CatTinder Team")
                                .email("support@cattinder.com")));
    }
}