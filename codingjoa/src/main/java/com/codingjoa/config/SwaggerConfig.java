package com.codingjoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	private static final String API_NAME = "Codingjoa API";
	private static final String API_DESCRIPTION  = "Codingjoa API specification";
	private static final String API_VERSION = "1.0.0";
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			//.apis(RequestHandlerSelectors.basePackage("com.codingjoa.controller"))
			.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(API_NAME)
			.description(API_DESCRIPTION)
			.version(API_VERSION)
			.build();
	}
	
}
