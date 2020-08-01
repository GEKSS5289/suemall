package com.sue.config;

import org.springframework.beans.factory.annotation.Configurable;
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
public class Swagger {

    @Bean
    public Docket CreateRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))
                .paths(PathSelectors.any())
                .build();
    }



    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("sue-mall-jpa")
                .description("sue-mall提供的api")
                .version("1.0.0")
                .termsOfServiceUrl("https://www.shsuhun.com")
                .contact(new Contact("shsuhun","https://496693","fsafa"))
                .build();
    }

}
