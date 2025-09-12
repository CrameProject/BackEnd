package com.backend.crame.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(Environment env) {
		boolean isProd = Arrays.asList(env.getActiveProfiles()).contains("prod");

		List<Server> servers = new ArrayList<>();
		servers.add(new Server().url("https://api.crame.site").description("Production API"));
		if (!isProd) {
			servers.add(new Server().url("http://localhost:8080").description("Local Dev"));
		}

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

		return new OpenAPI()
				.components(new Components())
				.info(apiInfo())
				.addSecurityItem(securityRequirement)
				.schemaRequirement("BearerAuth", securityScheme())
				.servers(servers);
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