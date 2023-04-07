package com.codingjoa.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "criteriaValidator")
public class CriteriaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CriteriaValidator.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== CriteriaValidator, {} ==============", errors.getObjectName());
	}

}
