package com.luana.centros_comunitarios.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gestão de Centros Comunitários",
                version = "1.0",
                description = "API para gerenciamento de ocupação e recursos em centros comunitários durante emergências"
        )
)
public class SwaggerConfig {
}
