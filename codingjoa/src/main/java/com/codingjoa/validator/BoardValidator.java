package com.codingjoa.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "boardValidator")
public class BoardValidator implements Validator {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return BoardDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== BoardValidator validate: {} ==============", errors.getObjectName());
		
		BoardDto boardDto = (BoardDto) target;
		String objectName = errors.getObjectName();
		
		if (objectName.equals("writeBoardDto")) {
			
		} else if (objectName.equals("modifyBoardDto")) {
			
		}
		
		int categoryCode = boardDto.getBoardCategoryCode();
		
		if (!categoryService.isBoardCategory(categoryCode)) {
			errors.rejectValue("boardCategoryCode", "NotBoard");
			return;
		}
		
		if (!StringUtils.hasText(boardDto.getBoardTitle())) {
			errors.rejectValue("boardTitle", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(boardDto.getBoardContent())) {
			errors.rejectValue("boardContent", "NotBlank");
			return;
		}
		
		List<Integer> uploadIdxList = boardDto.getUploadIdxList();
		
		if (uploadIdxList == null) return;
		
		for (int uploadIdx : uploadIdxList) {
			if (!boardService.isImageUploaded(uploadIdx)) {
				errors.rejectValue("boardContent", "NotUploadImage");
				return;
			}
		}
		
	}
	
	private Integer getCurrentIdx() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) return null;

		Object principal = auth.getPrincipal();
		Integer currentIdx = null;

		if (principal instanceof UserDetailsDto) {
			UserDetailsDto userDetailsDto = (UserDetailsDto) principal;
			currentIdx = userDetailsDto.getMember().getMemberIdx();
		} else if (principal instanceof String) {
			currentIdx = null; // principal = anonymousUser
		}

		return currentIdx;
	}
}
