package com.alias.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Knife4j 接口文档配置
 */
@Configuration
@EnableSwagger2
@Profile({"dev", "prod"})   //版本控制访问
public class Knife4jConfig {
    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.alias.ai.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * 自定义接口文档信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 接口文档的标题
                .title("AI Plots")
                // 接口文档的描述信息
                .description("使用AI服务，调用接口帮助用户进行数据分析，生成可视化图表，并给出分析结论，提供同步和异步化的生成策略。")
                // 作者信息
                .termsOfServiceUrl("https://github.com/AliasJeff")
                .contact(new Contact("jeffery", "https://github.com/AliasJeff", "zhexunchen@qq.com"))
                // 版本
                .version("1.0")
                // 构建
                .build();
    }
}