package com.codingjoa.test;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestValidator implements Validator {

	private final int MIN = 4;
	private final int MAX = 10;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Test.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		Test test = (Test) target;
		int param4 = test.getParam4();
		
		if (param4 < MIN || param4 > MAX) {
			errors.rejectValue("param4", "NotBetween", new Object[] { MIN, MAX, param4 }, null);
		}
	}

}
