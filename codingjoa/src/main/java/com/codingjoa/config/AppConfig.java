package com.codingjoa.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

import com.codingjoa.converter.NullToEmptyStringSerializer;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@ComponentScan("com.codingjoa.service") // @TransactionEventListener
@ComponentScan("com.codingjoa.handler")	// @ControllerAdvice, @RestControllerAdvice
@Configuration
public class AppConfig {
	
	@Bean // thread-safe
	public ObjectMapper objectMapper() { 
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
        return Jackson2ObjectMapperBuilder
        		.json()
        		.serializerByType(Date.class, new DateSerializer(false, format))
        		//.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        		.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
        		.serializerByType(Object.class, new NullToEmptyStringSerializer())
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
	 * 
	 * Since Spring 3.1, PropertySourcesPlaceholderConfigurer is used internally, 
	 * and if it cannot find ${...} values, it retrieves properties from the Environment. 
	 * For cases where setting Environment variables is required, implementation through @PropertySource is needed.
	 * 
	 * @ https://spring.io/blog/2011/02/15/spring-3-1-m1-unified-property-management
	 * @ https://velog.io/@dailylifecoding/Spring-PropertySourcesPlaceholderConfigurer-%EB%8A%94-Environment.getProperty-%EB%A1%9C-%EC%9D%BD%EC%9D%84-%EC%88%98-%EC%97%86%EB%8B%A4
	 * It's the PropertySourcesPlaceholderConfigurer who uses Environment and not vice versa.
	 * In other words, PropertySourcesPlaceholderConfigurer uses Environment to resolve property placeholders, 
	 * but Environment does not use PropertySourcesPlaceholderConfigurer.
	 * 
	 * When PropertySourcesPlaceholderConfigurer is registered as a bean, 
	 * the BeanFactoryPostProcessor handles the configuration file loading and injection. 
	 * There were no issues with @Value or other lifecycle components that occur after the bean creation, 
	 * because they are processed after the BeanFactoryPostProcessor. 
	 * However, issues arise when the order of PropertySource loading could not be guaranteed. 
	 * The key method of EnvironmentPostProcessor, postProcessEnvironment(), is invoked at the point 
	 * when the ApplicationContext is being refreshed. 
	 * Therefore, it is executed somewhere between the start of the Spring application and before the context is fully configured, 
	 * ensuring that injection can be guaranteed before the properties are used by the libraries.
	 * 
	 * "Spring generally recommends property injection through EnvironmentPostProcessor"
	 * 
	 */
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(ApplicationContext context) {
		PropertySourcesPlaceholderConfigurer configuer = new PropertySourcesPlaceholderConfigurer();
		try {
			Resource[] resources = context.getResources("WEB-INF/properties/*.properties"); // resourcePatternResolver
			configuer.setLocations(resources);
			configuer.setFileEncoding("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return configuer;
	}
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
