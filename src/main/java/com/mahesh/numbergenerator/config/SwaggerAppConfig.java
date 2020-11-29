package com.mahesh.numbergenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
 
@Configuration
public class SwaggerAppConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mahesh.numbergenerator.controller"))
                .paths(PathSelectors.any())
                .build();
    }
 
    private ApiInfo getApiInfo() {
        Contact contact = new Contact("Mahesh Rao", "TBD", "maheshu.uvce@gmail.com");
        return new ApiInfoBuilder()
                .title("Number Generator API")
                .description("Generates a sequence of numbers in the decreasing order till 0 starting from goal and reducing by step value.")
                .version("1.0.0")
                .license("")
                .licenseUrl("")
                .contact(contact)
                .build();
    }

}
