package com.codingjoa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.codingjoa.annotation.BoardCategoryCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "boardCategoryCodeValidator")
public class BoardCategoryCodeValidator implements ConstraintValidator<BoardCategoryCode, String> {
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		log.info("============== BoardCategoryCodeValidator ==============");
		
		return false;
	}

}
