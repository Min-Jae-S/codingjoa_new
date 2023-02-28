package com.codingjoa.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.CommentDto;
import com.codingjoa.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "commentValidator")
public class CommentValidator implements Validator {

	@Autowired
	private CommentService commentService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CommentDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== CommentValidator, {} ==============", errors.getObjectName());
		
		CommentDto commentDto = (CommentDto) target;
		
		
		
		
		
	}

}
