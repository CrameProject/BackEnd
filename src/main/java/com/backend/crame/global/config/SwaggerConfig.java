package com.backend.crame.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Component
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

		return new OpenAPI()
				.components(new Components())
				.info(apiInfo())
				.addSecurityItem(securityRequirement)
				.schemaRequirement("BearerAuth", securityScheme())
				.servers(List.of(
						new Server().url("https://api.crame.site").description("Production API")
				));
	}

	private Info apiInfo() {
		return new Info()
				.title("Crame API")
				.description("Crame팀 API 명세서입니다")
				.version("1.0.0");
	}

	private SecurityScheme securityScheme() {
		return new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
				.in(SecurityScheme.In.HEADER)
				.name("Authorization");
	}
}