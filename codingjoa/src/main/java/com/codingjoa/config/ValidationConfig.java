package com.codingjoa.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.codingjoa.annotation.BoardCategoryCode;

@Configuration
@ComponentScan(basePackages = "com.codingjoa.validator")
public class ValidationConfig {
	
	@Bean
	public Validator validator() {
		System.out.println("## ValidationConfig - validator()");
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		
		return localValidatorFactoryBean;
	}
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		System.out.println("## ValidationConfig - methodValidationPostProcessor()");
		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
		methodValidationPostProcessor.setValidatedAnnotationType(BoardCategoryCode.class);
		
		return new MethodValidationPostProcessor();
	}
	
}
