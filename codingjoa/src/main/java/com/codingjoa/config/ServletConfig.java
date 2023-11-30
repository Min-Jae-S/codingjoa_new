package com.codingjoa.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.validation.Validator;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.codingjoa.interceptor.ResetPasswordInterceptor;
import com.codingjoa.interceptor.TopMenuInterceptor;
import com.codingjoa.interceptor.UpdatePasswordInterceptor;
import com.codingjoa.resolver.BoardCriteriaArgumentResolver;
import com.codingjoa.resolver.CommentCriteriaArgumentResolver;
import com.codingjoa.resolver.MyExceptionResolver;
import com.codingjoa.service.CategoryService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebMvc 
@EnableTransactionManagement
@Slf4j
@PropertySource("/WEB-INF/properties/upload-path.properties")
@PropertySource("/WEB-INF/properties/criteria.properties")
@ComponentScan("com.codingjoa.controller")
@ComponentScan("com.codingjoa.service")
@ComponentScan("com.codingjoa.response") // @ControllerAdvice, @RestControllerAdvice
public class ServletConfig implements WebMvcConfigurer {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private RedisService redisService;
	
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
		jsonView.setObjectMapper(myObjectMapper());
        return jsonView;
    }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:///" + env.getProperty("image.root.path")); // D:/Dev/image/
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		WebMvcConfigurer.super.configurePathMatch(configurer);
		configurer.setUseTrailingSlashMatch(true);
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
		registry.addInterceptor(new TopMenuInterceptor(applicationContext, categoryService))
				.addPathPatterns("/**")
				.excludePathPatterns("/resources/**", "/upload/**", "/api/**");
		registry.addInterceptor(new UpdatePasswordInterceptor())
				.addPathPatterns("/member/account/updatePassword", "/api/member/password");
		registry.addInterceptor(new ResetPasswordInterceptor(redisService))
				.addPathPatterns("/member/resetPassword", "/api/member/reset/password");
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.info("## extendMessageConverters");
		converters.stream().forEach(converter -> { 
			log.info("\t > {}", converter.getClass().getSimpleName());
			if (converter instanceof StringHttpMessageConverter) {
				// StringHttpMessageConverter defaults to ISO-8859-1
				((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
			} else if (converter instanceof MappingJackson2HttpMessageConverter) {
				((MappingJackson2HttpMessageConverter) converter).setObjectMapper(myObjectMapper());
			}
		});
	}
	
	@Bean
	public ObjectMapper myObjectMapper() {
		return Jackson2ObjectMapperBuilder
				.json()
				//.deserializerByType(int.class, new IntDeserializer())
				.featuresToEnable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
				.build();
	}
	
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		log.info("## extendHandlerExceptionResolvers");
		WebMvcConfigurer.super.extendHandlerExceptionResolvers(resolvers);
		resolvers.add(0, myExceptionResolver());
		resolvers.forEach(resolver -> log.info("\t > {}", resolver.getClass().getSimpleName()));
	}
	
	@Bean
	public HandlerExceptionResolver myExceptionResolver() {
		return new MyExceptionResolver();
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		// MultipartResolver: StandardServletMultipartResolver, CommonsMultipartResolver
		// CommonsMultipartResolver 사용시 commons-fileupload 라이브러리 추가
		return new StandardServletMultipartResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
		resolvers.add(boardCriteriaArgumentResolver());
		resolvers.add(commentCriteriaArgumentResolver());
	}

	@SuppressWarnings("unchecked")
	@Bean
	public HandlerMethodArgumentResolver boardCriteriaArgumentResolver() {
		BoardCriteriaArgumentResolver resolver = new BoardCriteriaArgumentResolver();
		resolver.setPage(env.getProperty("criteria.board.page", Integer.class));
		resolver.setRecordCnt(env.getProperty("criteria.board.recordCnt", Integer.class));
		resolver.setType(env.getProperty("criteria.board.type"));
		
		String jsonRecordCntMap = env.getProperty("criteria.board.recordCntMap");
		String jsonTypeMap = env.getProperty("criteria.board.typeMap");
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
	public HandlerMethodArgumentResolver commentCriteriaArgumentResolver() {
		CommentCriteriaArgumentResolver resolver = new CommentCriteriaArgumentResolver();
		resolver.setPage(env.getProperty("criteria.comment.page", Integer.class));
		resolver.setRecordCnt(env.getProperty("criteria.comment.recordCnt", Integer.class));
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
	
	/* 
	 * Classes that implement the BeanPostProcessor interface are instantiated on startup, 
	 * as part of the special startup phase of the ApplicationContext, before any other beans.
	 * Enable @Valid validation exception handler for @PathVariable, @RequestParam and @RequestHeader.
	 * mvcValidator, LocalValidatorFactoryBean, @Qualifier("localValidator")
	 */
	
	@Bean
	public static MethodValidationPostProcessor methodValidationPostProcessor(@Lazy Validator validator) {
		log.info("## methodValidationPostProcessor");
		log.info("\t > validator = {}", validator);
		log.info("\t > proxy validator ? {}", AopUtils.isAopProxy(validator));
		
		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setValidator(validator);
		processor.setProxyTargetClass(true);
		log.info("\t > isProxyTargetClass = {}", processor.isProxyTargetClass());
		
		/* 
		 * Spring internally uses a library that can generate class-based proxies, 
		 * allowing the creation of proxies even for classes that don't implement interfaces. 
		 * Hence, in general cases, Spring is capable of creating proxies and 
		 * enabling method-level validation even without explicitly setting setProxyTargetClass(true).
		 * However, there are certain scenarios where the generation of interface-based proxies is limited.
		 * For example, it is not possible to create an interface-based proxy for classes 
		 * that are declared as final, or for methods that are final or private.
		 * In such cases, you can enable the creation of class-based proxies by setting setProxyTargetClass(true). 
		 * This allows the MethodValidationPostProcessor to generate class-based proxies 
		 * and apply validation at the method level even for classes that cannot be proxied using interfaces. 
		 * Therefore, by using setProxyTargetClass(true), you can overcome the limitations of
		 * interface-based proxy generation and ensure that method-level validation works even 
		 * in scenarios where interface proxies are not feasible.
		 * 
		 */
		
		return processor;
	}

	/*
	 * @@ https://docs.jboss.org/hibernate/validator/5.1/reference/en-US/html/chapter-message-interpolation.html#section-resource-bundle-locator
	 * @@ https://stackoverflow.com/questions/11225023/messageinterpolator-in-spring
	 * @@ https://stackoverflow.com/questions/3587317/autowiring-a-service-into-a-validator 
	 * 
	 * In Spring, you need to obtain ValidatorFactory (or Validatoritself) via LocalValidatorFactoryBean 
	 * instead of Validation.buildDefaultValidatorFactory(), as described in the reference.
	 */
	
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		factoryBean.setValidationMessageSource(messageSource());
		//factoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
		return factoryBean;
	}

//	@Override
//	public void addFormatters(FormatterRegistry registry) {
//		log.info("## addFormatters");
//		registry.addConverter(new TestConverter());
//	}
	
}
