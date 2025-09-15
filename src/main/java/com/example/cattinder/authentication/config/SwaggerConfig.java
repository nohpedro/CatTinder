package com.example.cattinder.authentication.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CatTinder Authentication API")
                        .version("1.0.0")
                        .description("API for user authentication and token management in CatTinder application")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@cattinder.com")
                                .url("https://www.cattinder.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.cattinder.com")
                                .description("Production Server")
                ));

    }
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("authentication")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }
    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .displayName("All APIs")
                .build();
    }

}