package com.accenture.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pizzeria")
                        .version("1.0")
                        .description("Gestion de la pizzeria")
                        .contact(new Contact()
                                .name("Tatiana")
                                .email("tatiana.m.tessier@accenture.com")
                                .url("http://localhost:8080/swagger-ui/index.html?continue#/")));
//                .components(new Components()
//                        .addSecuritySchemes("basicAuth",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("basic")))
//
//                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }


}