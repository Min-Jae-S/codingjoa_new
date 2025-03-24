package com.codingjoa.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.CommentDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CommentDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		CommentDto commentDto = (CommentDto) target;
		String content = commentDto.getContent();
		
		if (!StringUtils.hasText(content)) {
			errors.rejectValue("content", "NotBlank");
			return;
		} 
		
	}

}
