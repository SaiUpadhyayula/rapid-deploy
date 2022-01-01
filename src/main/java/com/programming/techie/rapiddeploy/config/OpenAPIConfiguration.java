package com.programming.techie.rapiddeploy.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI rapidDeployOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Rapid Deploy API")
                        .description("Rapid Deploy")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://example.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Rapid Deploy Wiki Documentation")
                        .url("https://example.com"));
    }
}
