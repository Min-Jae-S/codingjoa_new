package com.codingjoa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.codingjoa.validator")
public class ValidationConfig {
	
//	@Bean
//	public Validator validator() {
//		System.out.println("## ValidationConfig - validator()");
//		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
//		
//		return localValidatorFactoryBean;
//	}
	
//	@Bean
//	public static MethodValidationPostProcessor methodValidationPostProcessor() {
//		System.out.println("## ValidationConfig - methodValidationPostProcessor()");
//		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
//		methodValidationPostProcessor.setValidatedAnnotationType(BoardCategoryCode.class);
//		
//		return new MethodValidationPostProcessor();
//	}
	
}
