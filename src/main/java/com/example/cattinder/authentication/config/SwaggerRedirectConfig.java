package com.example.cattinder.authentication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerRedirectConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect to swagger-ui.html
        registry.addViewController("/swagger-ui")
                .setViewName("forward:/swagger-ui.html");

        registry.addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui.html");

        registry.addViewController("/api-docs-ui")
                .setViewName("forward:/swagger-ui.html");
    }
}