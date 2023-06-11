package com.codingjoa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codingjoa.annotation.BoardCategoryCode;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "boardCategoryCodeValidator")
public class BoardCategoryCodeValidator implements ConstraintValidator<BoardCategoryCode, Integer> {
	
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		log.info("\t > raw boardCategoryCode = {}", value);
		
		return (value == null) ? false : categoryService.isBoardCategoryCode(value);
		//return categoryService.isBoardCategoryCode(value);
	}
}
