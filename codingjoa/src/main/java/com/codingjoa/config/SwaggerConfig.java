package com.codingjoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
	
	private static final String API_TITLE = "Codingjoa API";
	private static final String API_DESCRIPTION = "Codingjoa API specification";
	private static final String API_VERSION = "1.0.0";
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Swagger UI (3.0.0)
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
		
		// Swagger UI (2.9.2)
//		registry.addResourceHandler("swagger-ui.html")
//				.addResourceLocations("classpath:/META-INF/resources/");
//		registry.addResourceHandler("/webjars/**")
//				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// swagger2: {protocol}://{host}:{port}/{contextPath}/swagger-ui.html
		// swagger3: {protocol}://{host}:{port}/{contextPath}/swagger-ui/index.html
		registry.addViewController("/swagger-ui/api-docs").setViewName("forward:/swagger-ui/index.html"); 	
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			//.apis(RequestHandlerSelectors.basePackage("com.codingjoa.controller"))
			.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo())
			.useDefaultResponseMessages(false);
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(API_TITLE)
			.description(API_DESCRIPTION)
			.version(API_VERSION)
			.build();
	}
	
}
