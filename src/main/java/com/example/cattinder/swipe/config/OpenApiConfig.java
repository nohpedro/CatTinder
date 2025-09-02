package com.example.cattinder.swipe.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Expone la UI en /openapi/swagger-ui.html y el JSON en /openapi/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${api.common.version}")
    String apiVersion;

    @Value("${api.common.title}")
    String apiTitle;
    @Value("${api.common.description}")
    String apiDescription;
    @Value("${api.common.termsOfService}")
    String apiTermsOfService;
    @Value("${api.common.license}")
    String apiLicense;
    @Value("${api.common.licenseUrl}")
    String apiLicenseUrl;
    @Value("${api.common.externalDocDesc}")
    String apiExternalDocDesc;
    @Value("${api.common.externalDocUrl}")
    String apiExternalDocUrl;
    @Value("${api.common.contact.name}")
    String apiContactName;
    @Value("${api.common.contact.url}")
    String apiContactUrl;
    @Value("${api.common.contact.email}")
    String apiContactEmail;

    /** UI: $HOST:$PORT/openapi/swagger-ui.html */
    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
            .info(new Info()
                .title(apiTitle)
                .description(apiDescription)
                .version(apiVersion)
                .termsOfService(apiTermsOfService)
                .license(new License().name(apiLicense).url(apiLicenseUrl))
                .contact(new Contact().name(apiContactName).url(apiContactUrl).email(apiContactEmail))
            )
            .externalDocs(new ExternalDocumentation().description(apiExternalDocDesc).url(apiExternalDocUrl));
    }
}
