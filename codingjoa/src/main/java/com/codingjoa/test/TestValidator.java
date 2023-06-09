package com.codingjoa.test;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Test.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		
		if (errors.hasFieldErrors("param3")) {
			log.info("\t > param3 has error");
			return;
		}
	}

}
