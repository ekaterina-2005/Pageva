package com.app.system;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Настройка Swagger (http://localhost:8080/swagger-ui.html)
@Configuration
public class OpenApiConfig {

    public static final String BASIC = "basicAuth";
    public static final String BEARER = "bearerAuth";

    @Bean
    public OpenAPI libraryOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library API")
                        .version("1.0")
                        .description("API библиотеки: книги, экземпляры, читатели и выдачи"))
                .components(new Components()
                        .addSecuritySchemes(BASIC, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP).scheme("basic"))
                        .addSecuritySchemes(BEARER, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER));
    }
}
