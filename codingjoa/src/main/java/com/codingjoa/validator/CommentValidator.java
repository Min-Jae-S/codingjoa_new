package com.codingjoa.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.CommentDto;
import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "commentValidator")
public class CommentValidator implements Validator {

	@Autowired
	private BoardService boardService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CommentDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== CommentValidator ==============");
		
		CommentDto commentDto = (CommentDto) target;
		int boardIdx = commentDto.getCommentBoardIdx();
		int boardCategoryCode = commentDto.getBoardCategoryCode();
		
//		if (!StringUtils.isNumeric(boardIdx) || !StringUtils.isNumeric(boardCategoryCode)) {
//			...
//		}
		
		if (!boardService.isBoardIdxExist(boardIdx, boardCategoryCode)) {
			errors.rejectValue("commentBoardIdx", "NotBoardIdxExist");
			return;
		}
		
		if (!StringUtils.hasText(commentDto.getCommentContent())) {
			errors.rejectValue("commentContent", "NotBlank");
			return;
		}
		
	}

}
