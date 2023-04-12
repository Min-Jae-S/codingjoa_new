package com.codingjoa.config;

import javax.validation.Validator;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@Import(MethodValidationPostProcessor.class)
@ComponentScan(basePackages = "com.codingjoa.validator")
public class ValidationConfig {

//	@Bean
//	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//	public static LocalValidatorFactoryBean validator() {
//		System.out.println("## ValidationConfig - LocalValidatorFactoryBean");
//		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
//		return factoryBean;
//	}
//	
//	@Bean
//	public static MethodValidationPostProcessor methodValidationPostProcessor(@Lazy Validator validtor) {
//		System.out.println("## ValidationConfig - methodValidationPostProcessor");
//		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
//		processor.setValidator(validtor);
//		return processor;
//	}
}
