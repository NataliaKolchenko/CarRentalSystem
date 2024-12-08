package com.example.CarRentalSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(List.of(new io.swagger.v3.oas.models.servers.Server().url("http://localhost:11000")))
                .info(new Info()
                        .title("CarRentalSystem API")
                        .description("API documentation for Car Rental System")
                        .version("1.0"));
    }
}
