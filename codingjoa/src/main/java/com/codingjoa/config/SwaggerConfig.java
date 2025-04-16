package com.codingjoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.codingjoa.controller"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo())
			// Unable to infer base url. This is common when using dynamic servlet registration or when the API is behind an API Gateway. 
			// The base url is the root of where all the swagger resources are served. 
			// For e.g. if the api is available at http://example.org/api/v2/api-docs then the base url is http://example.org/api/. Please enter the location manually: 
			.pathMapping("/codingjoa");
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("CODINGJOA REST API")
			.description("CODINGJOA | SWAGGER TEST")
			.version("1.0.0")
			.build();
	}
	
}
