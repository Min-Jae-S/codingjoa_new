package com.codingjoa.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.CommentDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "commentValidator")
public class CommentValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CommentDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > objectName = {}", errors.getObjectName());
		
		CommentDto commentDto = (CommentDto) target;
//		if (!boardService.isBoardIdxExist(commentBoardIdx, boardCategoryCode)) {
//			errors.rejectValue("commentBoardIdx", "NotBoardIdxExist");
//			return;
//		}
		
		if (!StringUtils.hasText(commentDto.getCommentContent())) {
			errors.rejectValue("commentContent", "NotBlank");
			return;
		}
	}
}
