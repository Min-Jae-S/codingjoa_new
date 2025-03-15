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
		log.info("## {}, value = {}", this.getClass().getSimpleName(), value);
		
		// value can't be null? @BoardCategoryCode @RequestParam int boardCategoryCode
		if (value == null) {
			return false;
		}
		
		return categoryService.getBoardCategories().stream()
				.anyMatch(category -> category.getCode().equals(value));
	}
	
}
