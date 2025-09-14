package com.example.cattinder.authentication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CatTinder Authentication API")
                        .version("1.0")
                        .description("API for user authentication in CatTinder application")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@cattinder.com")));
    }
}