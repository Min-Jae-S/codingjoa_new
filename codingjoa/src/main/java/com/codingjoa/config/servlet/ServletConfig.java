package com.codingjoa.config.servlet;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.validation.Validator;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.codingjoa.error.PreExceptionHandlerExceptionResolver;
import com.codingjoa.interceptor.PasswordResetViewInterceptor;
import com.codingjoa.interceptor.TopMenuInterceptor;
import com.codingjoa.service.CategoryService;
import com.codingjoa.service.RedisService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ComponentScan({
	"com.codingjoa.controller",
	"com.codingjoa.resolver",
	"com.codingjoa.error"
})
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@EnableWebMvc 
@Configuration
public class ServletConfig implements WebMvcConfigurer {
	
	private final Environment env;
	private final CategoryService categoryService;
	private final RedisService redisService;
	private final MessageSource messageSource;
	private final ObjectMapper objectMapper;
	private final List<HandlerMethodArgumentResolver> argumentResolvers; 

	// instance class --> interface (issue at proxy, AOP)
	//private final HandlerExceptionResolver preHandlerExceptionResolver;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
		//registry.viewResolver(beanNameViewResolver());
	}
	
	@Bean
	public ViewResolver beanNameViewResolver() {
		BeanNameViewResolver resolver = new BeanNameViewResolver();
		resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return resolver;
	}
	
	@Bean
	public MappingJackson2JsonView jsonView() {
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setObjectMapper(objectMapper);
		jsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);
		jsonView.setEncoding(JsonEncoding.UTF8);
        return jsonView;
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/")
				.setCachePeriod(0);
		registry.addResourceHandler("/user/images/**")
				.addResourceLocations("file:///" + env.getProperty("upload.dir.user.image")); 	// D:/Dev/upload/user/images/
		registry.addResourceHandler("/board/images/**")
				.addResourceLocations("file:///" + env.getProperty("upload.dir.board.image")); 	// D:/Dev/upload/board/images/
	}
	
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		WebMvcConfigurer.super.configurePathMatch(configurer);
		//configurer.setUseTrailingSlashMatch(true);
		
		// @24.04.02 변경
		// GetMapping(value = { "/comments/", "/comments/{commentIdx}" })
		configurer.setUseTrailingSlashMatch(false);
		
		// @PathVariable을 사용하여 dot(.)이 포함된 요청 URI에서 매개변수에서 dot 이후까지 완전히 포함하기 위한 설정
		//configurer.setUseSuffixPatternMatch(false); // In 5.3 the default becomes false
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		WebMvcConfigurer.super.configureContentNegotiation(configurer);
		// *.jpg에 의한 요청에서 exception 발생 시 ContentType이 "json"이 아닌 null인 문제점을 해결하기 위한 설정
		configurer.favorPathExtension(false); // In 5.3 the default becomse false
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(new TopMenuInterceptor(categoryService))
				.addPathPatterns("/**")
				.excludePathPatterns("/api/**", "/resources/**", "/user/images/**", "/board/images/**", "/v2/api-docs");
		registry.addInterceptor(new PasswordResetViewInterceptor(redisService))
				.addPathPatterns("/password/reset");
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.info("## extendMessageConverters");
		converters.stream().forEach(converter -> { 
			if (converter instanceof StringHttpMessageConverter) {
				// StringHttpMessageConverter defaults to ISO-8859-1
				((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
			} else if (converter instanceof MappingJackson2HttpMessageConverter) {
				((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
			}
		});
	}
	
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		log.info("## extendHandlerExceptionResolvers");
		PreExceptionHandlerExceptionResolver preResolver = null;
		
		for (HandlerExceptionResolver resolver : resolvers) {
			if (resolver instanceof ExceptionHandlerExceptionResolver) {
				ExceptionHandlerExceptionResolver baseResolver = (ExceptionHandlerExceptionResolver) resolver;
				preResolver = new PreExceptionHandlerExceptionResolver(baseResolver);
				preResolver.afterPropertiesSet();
				break;
			}
		}
		
		if (preResolver != null) {
			resolvers.add(0, preResolver);
		}
		
		resolvers.forEach(resolver -> log.info("\t > {}", resolver.getClass().getSimpleName()));
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		log.info("## addArgumentResolvers");
		resolvers.addAll(argumentResolvers);
		resolvers.forEach(resolver -> log.info("\t > {}", resolver.getClass().getSimpleName()));
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		// MultipartResolver: StandardServletMultipartResolver, CommonsMultipartResolver
		// add the commons-fileupload library when using CommonsMultipartResolver
		return new StandardServletMultipartResolver();
	}
	
	/* 
	 * ## mvcValidator, LocalValidatorFactoryBean - @Qualifier("localValidator")
	 * 
	 * Classes that implement the BeanPostProcessor interface are instantiated on startup, 
	 * as part of the special startup phase of the ApplicationContext, before any other beans.
	 * Enable @Valid validation exception handler for @PathVariable, @RequestParam and @RequestHeader.
	 * 
	 */
	
	@Bean
	public static MethodValidationPostProcessor methodValidationPostProcessor(
			@Lazy @Qualifier("localValidator") Validator validator) {
		log.info("## methodValidationPostProcessor");
		//log.info("\t > validator = {}", validator);
		log.info("\t > isAopProxy = {}", AopUtils.isAopProxy(validator));
		log.info("\t > target class = {}", AopProxyUtils.ultimateTargetClass(validator));
		
		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setValidator(validator);
		processor.setProxyTargetClass(true);
		//log.info("\t > isProxyTargetClass = {}", processor.isProxyTargetClass());
		return processor;
		
		/* 
		 * Spring internally uses a library that can generate class-based proxies, 
		 * allowing the creation of proxies for classes that don't implement interfaces. 
		 * Hence, in general cases, Spring is capable of creating proxies and enabling method-level validation 
		 * even without explicitly setting setProxyTargetClass(true).
		 * However, there are certain scenarios where the generation of interface-based proxies is limited.
		 * For example, it is not possible to create an interface-based proxy for classes 
		 * that are declared as final, or for methods that are final or private.
		 * In such cases, you can enable the creation of class-based proxies by setting setProxyTargetClass(true). 
		 * This allows the MethodValidationPostProcessor to generate class-based proxies 
		 * and apply validation at the method level even for classes that cannot be proxied using interfaces. 
		 * Therefore, by using setProxyTargetClass(true), you can overcome the limitations of
		 * interface-based proxy generation and ensure that method-level validation works even 
		 * in scenarios where interface proxies are not feasible.
		 */
	}

	/*
	 * # https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html/chapter-message-interpolation.html#section-resource-bundle-locator
	 * # https://stackoverflow.com/questions/11225023/messageinterpolator-in-spring
	 * # https://stackoverflow.com/questions/3587317/autowiring-a-service-into-a-validator 
	 * 
	 * In Spring, you need to obtain ValidatorFactory (or Validatoritself) via LocalValidatorFactoryBean 
	 * instead of Validation.buildDefaultValidatorFactory(), as described in the reference.
	 */
	
	@Bean
	public LocalValidatorFactoryBean localValidator() {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		factoryBean.setValidationMessageSource(messageSource);
		//factoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
		return factoryBean;
	}


}
