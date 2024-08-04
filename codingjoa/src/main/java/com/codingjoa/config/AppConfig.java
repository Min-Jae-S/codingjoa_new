package com.codingjoa.config;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@ComponentScan("com.codingjoa.security")
@ComponentScan("com.codingjoa.service") 	// @TransactionEventListener
@ComponentScan("com.codingjoa.response")	// @ControllerAdvice, @RestControllerAdvice
@Configuration
public class AppConfig {
	
	@Bean // thread-safe
	public ObjectMapper objectMapper() { 
		return Jackson2ObjectMapperBuilder
				.json()
				.featuresToEnable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES) // writeComment - CommentDto(commentBoardIdx)
				.build();
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setDefaultEncoding("UTF-8");
		source.setBasenames(
				"/WEB-INF/properties/error-message", 
				"/WEB-INF/properties/success-message",
				"/WEB-INF/properties/validation-message");
		return source;
	}
	
	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource());
	}
	
	@Bean
	public MessageUtils messageUtils() {
		MessageUtils messageUtils = new MessageUtils();
		messageUtils.setMessageSourceAccessor(messageSourceAccessor());
		return messageUtils;
	}
	
	/*
	 * PropertySourcesPlaceholderConfigurer is a BeanFactoryPostProcessor that performs the task of replacing ${} placeholders
	 * after the bean factory has finished creating all bean definitions, and then proceeds to create the bean objects.
	 * ex) @Value("${...}") within @Configuration classes
	 * 
	 * @ https://mangkyu.tistory.com/177
	 * @ https://mangkyu.tistory.com/214
	 * Special consideration must be taken for @Bean methods that return Spring BeanFactoryPostProcessor (BFPP) types. 
	 * Because BFPP objects must be instantiated very early in the container lifecycle, 
	 * they can interfere with processing of annotations such as @Autowired, @Value, and @PostConstruct within @Configuration classes. 
	 * To avoid these lifecycle issues, mark BFPP-returning @Bean methods as static.
	 * By marking this method as static, it can be invoked without causing instantiation of its declaring @Configuration class, 
	 * thus avoiding the above-mentioned lifecycle conflicts.
	 */
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(
			ApplicationContext applicationContext) {
		PropertySourcesPlaceholderConfigurer configuer = new PropertySourcesPlaceholderConfigurer();
		try {
			Resource[] resources = applicationContext.getResources("WEB-INF/properties/*.properties"); // resourcePatternResolver
			configuer.setLocations(resources);
			configuer.setFileEncoding("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return configuer;
	}
}
