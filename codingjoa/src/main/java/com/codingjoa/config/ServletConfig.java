package com.codingjoa.config;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codingjoa.resolver.BoardCriteriaArgumentResolver;
import com.codingjoa.resolver.CommentCriteriaArgumentResolver;
import com.codingjoa.resolver.CustomExceptionResolver;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebMvc
@PropertySource("/WEB-INF/properties/upload.properties")
@PropertySource("/WEB-INF/properties/criteria.properties")
@ComponentScan(basePackages = {	"com.codingjoa.controller", "com.codingjoa.validator" })
public class ServletConfig implements WebMvcConfigurer {
	
	@Autowired
	private Environment env;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:///" + env.getProperty("upload.path"));
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		log.info("-------- configurePathMatch --------");
		
		configurer.setUseTrailingSlashMatch(true);
	    log.info("	isUseTrailingSlashMatch = {}", configurer.isUseTrailingSlashMatch());
	    log.info("	isUseSuffixPatternMatch = {}", configurer.isUseSuffixPatternMatch());
	    log.info("	isUseRegisteredSuffixPatternMatch = {}", configurer.isUseRegisteredSuffixPatternMatch());

	    WebMvcConfigurer.super.configurePathMatch(configurer);
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.info("-------- extendMessageConverters --------");
//		converters.add(0, mappingJackson2HttpMessageConverter());
//		converters.stream()
//			.filter(converter -> converter instanceof StringHttpMessageConverter)
//			.forEach(converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
		
		// StringHttpMessageConverter defaults to ISO-8859-1
		converters.stream()
			.forEach(converter -> {
				if (converter instanceof StringHttpMessageConverter) {
					 ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
				} else if (converter instanceof MappingJackson2HttpMessageConverter) {
					 ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper());
				}
			});
		
		for (HttpMessageConverter<?> converter : converters) {
			log.info("	{}", converter.getClass().getSimpleName());
		}
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.timeZone(TimeZone.getTimeZone("Asia/Seoul")) // @JsonFormat(timezone = "Asia/Seoul")
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
				.build();
		
		return objectMapper;
	}
	
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		log.info("-------- extendHandlerExceptionResolvers --------");
		
		resolvers.add(0, new CustomExceptionResolver());
		for (HandlerExceptionResolver resovler : resolvers) {
			log.info("	{}", resovler.getClass().getSimpleName());
		}
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		// MultipartResolver: StandardServletMultipartResolver, CommonsMultipartResolver
		// CommonsMultipartResolver 사용시 commons-fileupload 라이브러리 추가
		return new StandardServletMultipartResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(boardCriteriaArgumentResolver());
		resolvers.add(commentCriteriaArgumentResolver());
	}

	@SuppressWarnings("unchecked")
	@Bean
	public BoardCriteriaArgumentResolver boardCriteriaArgumentResolver() {
		BoardCriteriaArgumentResolver resolver = new BoardCriteriaArgumentResolver();
		resolver.setPage(env.getProperty("criteria.page", Integer.class));
		resolver.setRecordCnt(env.getProperty("criteria.recordCnt", Integer.class));
		resolver.setType(env.getProperty("criteria.type"));
		
		String jsonRecordCntMap = env.getProperty("criteria.recordCntMap");
		String jsonTypeMap = env.getProperty("criteria.typeMap");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			resolver.setRecordCntMap(objectMapper.readValue(jsonRecordCntMap, Map.class));
			resolver.setTypeMap(objectMapper.readValue(jsonTypeMap, Map.class));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return resolver;
	}

	@Bean
	public CommentCriteriaArgumentResolver commentCriteriaArgumentResolver() {
		CommentCriteriaArgumentResolver resolver = new CommentCriteriaArgumentResolver();
		resolver.setPage(env.getProperty("criteria.page", Integer.class));
		resolver.setRecordCnt(env.getProperty("criteria.recordCnt", Integer.class));
		
		return resolver;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setDefaultEncoding("UTF-8");
		source.setBasenames(
				"/WEB-INF/properties/error-message", 
				"/WEB-INF/properties/success-message",
				"/WEB-INF/properties/validation-message"
			);
		
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
	
	// https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html/chapter-message-interpolation.html#section-resource-bundle-locator
	// https://stackoverflow.com/questions/11225023/messageinterpolator-in-spring
	// https://stackoverflow.com/questions/3587317/autowiring-a-service-into-a-validator
	// In Spring, you need to obtain ValidatorFactory (or Validator itself) via LocalValidatorFactoryBean 
	// instead of Validation.buildDefaultValidatorFactory(), as described in the reference.
	@Bean
	public Validator validator() {
//		return Validation.byProvider(HibernateValidator.class)
//				.configure()
//				.messageInterpolator(
//						new ResourceBundleMessageInterpolator(
//								new MessageSourceResourceBundleLocator(messageSource())
//						)
//				)
//				.failFast(true)
//				.buildValidatorFactory()
//				.getValidator();
		
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		factoryBean.setValidationMessageSource(messageSource());
		//factoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
		
		return factoryBean;
	}
	
	// Enable @Valid validation exception handler for @PathVariable, @RequestParam and @RequestHeader.
	// mvcValidator, LocalValidatorFactoryBean, @Qualifier("localValidator")
	@Bean
	public static MethodValidationPostProcessor methodValidationPostProcessor(@Lazy Validator validator) { 
		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setValidator(validator);
		
		return processor;
	}
	
}
