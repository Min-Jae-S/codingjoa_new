package com.codingjoa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.codingjoa.annotation.BoardCategoryCode;
import com.codingjoa.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
//@Component(value = "boardCategoryCodeValidator")
public class BoardCategoryCodeValidator implements ConstraintValidator<BoardCategoryCode, Integer> {
	
	private final CategoryService categoryService;
	
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > value = {}", value);
		
		// value can't be null? ==> @BoardCategoryCode @RequestParam int boardCategoryCode
		return (value == null) ? false : categoryService.isBoardCategoryCode(value);
	}
}
