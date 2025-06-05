package com.example.Pill_Mate_Backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Configuration
public class SwaggerConfig {
    // Swagger UI: http://localhost:8080/swagger-ui/index.html#/

    //java타입 String으로 수정
    @PostConstruct
    public void customizeSchemas() {
        SpringDocUtils.getConfig()
                .replaceWithSchema(LocalDate.class, new StringSchema().example("2025-06-03").description("yyyy-MM-dd 형식의 날짜"))
                .replaceWithSchema(LocalTime.class, new StringSchema().example("15:30:00").description("HH:mm:ss 형식의 시간"))
                .replaceWithSchema(LocalDateTime.class, new StringSchema().example("2025-06-03T15:30:00").description("날짜 및 시간 (ISO 8601)"))
                .replaceWithSchema(OffsetDateTime.class, new StringSchema().example("2025-06-03T15:30:00+09:00").description("날짜, 시간 및 시간대 (ISO 8601)"));
    }

    @Bean
    public OpenAPI getOpenApi() {
        Server server = new Server().url("/");

        return new OpenAPI()
                .info(getSwaggerInfo())
                .components(getComponents())
                .components(authSetting()) // 보안 인증
                .addServersItem(server)
                .addSecurityItem(new SecurityRequirement().addList("access-token"));
    }

    private Info getSwaggerInfo() {
        License license = new License();
        license.setName("{Application}");

        return new Info()
                .title("Pill-Mate API Document")
                .description("Pill-Mate Server's API document.")
                .version("v0.0.1")
                .license(license);
    }

    private Components getComponents() {
        return new Components();
    }

    // 보안 인증 추가 시 사용
    private Components authSetting() {
        return new Components()
                .addSecuritySchemes(
                        "access-token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}
