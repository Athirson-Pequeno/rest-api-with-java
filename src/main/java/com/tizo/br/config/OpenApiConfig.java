package com.tizo.br.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Rest API with java and Spring")
                        .version("v1")
                        .description("A simulation API for managing the production flow of a company")
                        .license(new License().name("GitHub Repository")
                                .url("https://github.com/Athirson-Pequeno/rest-api-with-java"))
                );
    }
}
