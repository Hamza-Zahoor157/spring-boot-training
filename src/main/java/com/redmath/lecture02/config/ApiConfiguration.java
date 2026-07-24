package com.redmath.lecture02.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
    info = @Info(title = "News API"),
    security = @SecurityRequirement(name = "BearerAuth"))
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "BearerAuth", scheme = "bearer")
@Configuration
@EnableCaching
@EnableScheduling
@EnableAsync
public class ApiConfiguration {

}
