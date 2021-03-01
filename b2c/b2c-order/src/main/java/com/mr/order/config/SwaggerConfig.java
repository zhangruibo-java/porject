package com.mr.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        //指定访问地址 扫描的controler包，生成文档
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:8089")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mr.order.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //接口文档的标题，介绍版本号等，
        return new ApiInfoBuilder()
                .title("b2c商城订单系统")
                .description("b2c订单系统接口文档")
                .version("1.0")
                .build();
    }
}