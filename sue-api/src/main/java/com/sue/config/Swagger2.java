package com.sue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sue
 * @date 2020/8/1 10:24
 */

@Configuration
@EnableSwagger2
public class Swagger2 {
    //配置核心配置 docket
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2) //指定API类型为SWAGGER2
                .apiInfo(apiInfo())//用于定义api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("com.sue.controller"))
                .paths(PathSelectors.any()) //所有Controller
                .build();

    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("sue-mall 接口API")//文档页标题
                .contact(new Contact(
                        "imooc",
                        "https://www.imooc.com",
                        "496966859@qq.com"
                        )
                )
                .description("专为sue-mall提供的Api文档") //文档详细信息
                .version("1.0.1") //文档版本号
                .termsOfServiceUrl("https://www.imooc.com")//网站地址
                .build();
    }
}
