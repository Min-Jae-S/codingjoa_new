package com.codingjoa.config;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.codingjoa.resolver.BoardCriteriaArgumentResolver;
import com.codingjoa.resolver.CommentCriteriaArgumentResolver;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
@EnableWebMvc
@PropertySource("/WEB-INF/properties/upload.properties")
@ComponentScan(basePackages = { "com.codingjoa.controller", "com.codingjoa.validator", "com.codingjoa.resolver" })
public class ServletConfig implements WebMvcConfigurer {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private BoardCriteriaArgumentResolver criteriaArgumentResolver;
	
	@Autowired
	private CommentCriteriaArgumentResolver commentCriteriaArgumentResolver;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:///" + uploadPath);
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		WebMvcConfigurer.super.extendMessageConverters(converters);
		converters.add(0, mappingJackson2HttpMessageConverter());
		
		// StringHttpMessageConverter defaults to ISO-8859-1
		converters.stream()
			.filter(converter -> converter instanceof StringHttpMessageConverter)
			.forEach(converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.timeZone(TimeZone.getTimeZone("Asia/Seoul")) // @JsonFormat(timezone = "Asia/Seoul")
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
				//.deserializersByType(String.class, new StringTrimmerEditor(false))
				.build();
		
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new BeforeUpdatePasswordInterceptor())
//				.addPathPatterns("/member/updatePassword");
//		registry.addInterceptor(new CheckBoardCategoryInterceptor(categoryService))
//				.addPathPatterns("/board/main", "/board/write", "/board/writeProc", "/board/modifyProc");
//		registry.addInterceptor(new CheckBoardCategoryAndIdxInterceptor(boardService))
//				.addPathPatterns("/board/read");
//		registry.addInterceptor(new CheckMyBoardInterceptor(boardService))
//				.order(-1)
//				.addPathPatterns("/board/modify", "/board/modifyProc", "/board/deleteProc");
	}
	
	// MultipartResolver: StandardServletMultipartResolver, CommonsMultipartResolver
	// CommonsMultipartResolver 사용시 commons-fileupload 라이브러리 추가
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(criteriaArgumentResolver);
		resolvers.add(commentCriteriaArgumentResolver);
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
