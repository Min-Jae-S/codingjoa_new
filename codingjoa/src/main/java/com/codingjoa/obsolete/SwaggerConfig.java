package com.codingjoa.obsolete;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codingjoa.annotation.PrivateApi;
import com.codingjoa.security.dto.PrincipalDetails;

import io.swagger.annotations.Api;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
	
	private static final String API_TITLE = "Codingjoa APIs";
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
		registry.addViewController("/swagger-ui/apis").setViewName("forward:/swagger-ui/index.html");
	}
	
	@Bean
	public Docket allApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.groupName("all-apis")
			.apiInfo(apiInfo("전체 API 목록 (인증 필요 여부와 관계없이 모두 포함)"))
			.consumes(getConsumes())
			.produces(getProduces())
			.select()
			//.apis(RequestHandlerSelectors.basePackage("com.codingjoa.controller"))
			.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
			.paths(PathSelectors.any())
			.build()
			.ignoredParameterTypes(PrincipalDetails.class)
			.useDefaultResponseMessages(false)
			.securitySchemes(List.of(securityScheme()))
			.securityContexts(List.of(securityContext()));
	}

	@Bean
	public Docket publicApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.groupName("public-apis")
			.apiInfo(apiInfo("인증 없이 접근 가능한 API"))
			.consumes(getConsumes())
			.produces(getProduces())
			.select()
			.apis(getPublicPredicate())
			.paths(PathSelectors.any())
			.build()
			.ignoredParameterTypes(PrincipalDetails.class)
			.useDefaultResponseMessages(false)
			.securitySchemes(List.of(securityScheme()))
			.securityContexts(List.of(securityContext()));
	}

	@Bean
	public Docket privateApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.groupName("private-apis")
			.apiInfo(apiInfo("JWT 인증이 필요한 API"))
			.consumes(getConsumes())
			.produces(getProduces())
			.select()
			.apis(getPrivatePredicate())
			.paths(PathSelectors.any())
			.build()
			.ignoredParameterTypes(PrincipalDetails.class)
			.useDefaultResponseMessages(false)
			.securitySchemes(List.of(securityScheme()))
			.securityContexts(List.of(securityContext()));
	}

	private ApiInfo apiInfo(String description) {
		return new ApiInfoBuilder()
			.title(API_TITLE)
			.description(description)
			.version(API_VERSION)
			.build();
	}
	
	private Set<String> getConsumes() {
		return Set.of(
			MediaType.APPLICATION_JSON_VALUE, 				// application/json
			MediaType.APPLICATION_FORM_URLENCODED_VALUE		// application/x-www-form-urlencoded
		);
	}
	
	private Set<String> getProduces() {
		return Set.of(MediaType.APPLICATION_JSON_VALUE); 	// application/json
	}
	
	private Predicate<RequestHandler> getPublicPredicate() {
		Predicate<RequestHandler> isNotPrivateApi = RequestHandlerSelectors.withClassAnnotation(PrivateApi.class)
			.or(RequestHandlerSelectors.withMethodAnnotation(PrivateApi.class))
			.negate();
		return RequestHandlerSelectors.withClassAnnotation(Api.class).and(isNotPrivateApi);
	}
	
	private Predicate<RequestHandler> getPrivatePredicate() {
		Predicate<RequestHandler> isPrivateApi = RequestHandlerSelectors.withClassAnnotation(PrivateApi.class)
			.or(RequestHandlerSelectors.withMethodAnnotation(PrivateApi.class));
		return RequestHandlerSelectors.withClassAnnotation(Api.class).and(isPrivateApi);
	}
	
	private ApiKey securityScheme() {
		String headerKey = "Authorization";
		return new ApiKey("JWT", headerKey, "header");
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
			.securityReferences(securityReferences())
			.operationSelector(operationContext -> true)
			.build();
	}
	
	private List<SecurityReference> securityReferences() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	    return List.of(new SecurityReference("JWT", new AuthorizationScope[] { authorizationScope }));
	}
	
}
