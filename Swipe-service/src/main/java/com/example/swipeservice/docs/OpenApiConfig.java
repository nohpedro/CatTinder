package com.example.swipeservice.docs;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.swipeservice.docs.schemas.*;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI swipeServiceOpenAPI() {
        // Registra automáticamente los POJOs de docs.schemas como componentes (schemas)
        Components components = new Components();

        ModelConverters.getInstance().read(SwipeRequest.class)
                .forEach(components::addSchemas);
        ModelConverters.getInstance().read(SwipeCreatedResponse.class)
                .forEach(components::addSchemas);
        ModelConverters.getInstance().read(MatchView.class)
                .forEach(components::addSchemas);
        ModelConverters.getInstance().read(ApiError.class)
                .forEach(components::addSchemas);
        ModelConverters.getInstance().read(SwipeDirection.class)
                .forEach(components::addSchemas);

        SpringDocUtils.getConfig().replaceWithSchema(Map.class, null);

        return new OpenAPI()
                .info(new Info()
                        .title("Swipe Service API")
                        .version("v1")
                        .description("Esquemas y documentación de la API de swipes/matches"))
                .components(components);
    }
}
