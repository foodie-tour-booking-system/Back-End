package org.foodie_tour.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API Documentation")
                        .version("1.0")
                        .description("API quản lý hệ thống đặt lịch Tour"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")));
//                                        .bearerFormat("JWT")));
    }

    /**
     * Ghi đè schema của LocalTime trong Swagger UI thành string "HH:mm:ss"
     * thay vì hiển thị dạng object với các field hour/minute/second/nano.
     */
    @Bean
    public OpenApiCustomizer localTimeSchemaCustomizer() {
        return openApi -> {
            Schema<String> timeSchema = new Schema<>();
            timeSchema.setType("string");
            timeSchema.setFormat("HH:mm:ss");
            timeSchema.setExample("08:00:00");
            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }
            openApi.getComponents().addSchemas("LocalTime", timeSchema);
        };
    }
}
