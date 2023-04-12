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
public class BoardCategoryCodeValidator implements ConstraintValidator<BoardCategoryCode, Object> {
	
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		log.info("============== BoardCategoryCodeValidator ==============");
		log.info("{}", categoryService);
		
		boolean result = true;
		
		if (value instanceof String) {
			String rawBoardCategoryCode = String.valueOf(value);
			try {
				int boardCategoryCode = Integer.parseInt(rawBoardCategoryCode);
				if (!categoryService.isBoardCategory(boardCategoryCode)) {
					result = false;
				}
			} catch (NumberFormatException e) {
				result = false;
			}
		}
		
		return result;
	}
}
