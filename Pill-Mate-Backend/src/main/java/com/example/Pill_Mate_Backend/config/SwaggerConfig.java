package com.example.Pill_Mate_Backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    // url: http://localhost:8080/swagger-ui/index.html#/
    @Bean
    public OpenAPI getOpenApi() {
        Server server = new Server().url("/");
//            Server server = new Server().url("/");
        return new OpenAPI()
                .info(getSwaggerInfo())
                .components(getComponents())
                // 보안 인증 추가 시 사용
                .components(authSetting())
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