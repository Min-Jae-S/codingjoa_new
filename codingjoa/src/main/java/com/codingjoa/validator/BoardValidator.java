package com.codingjoa.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "boardValidator")
public class BoardValidator implements Validator {

	@Autowired
	private UploadService uploadService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return BoardDto.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > objectName = {}", errors.getObjectName());

		// boardIdx, boardCategoryCode
		if (errors.hasFieldErrors()) return; 

		BoardDto boardDto = (BoardDto) target;
		if (!StringUtils.hasText(boardDto.getBoardTitle())) {
			errors.rejectValue("boardTitle", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(boardDto.getBoardContent())) {
			errors.rejectValue("boardContent", "NotBlank");
			return;
		}
		
		if (!StringUtils.hasText(boardDto.getBoardContentText())) {
			errors.rejectValue("boardContent", "NotBlank");
			return;
		}
		
		List<Integer> uploadIdxList = boardDto.getUploadIdxList();
		if (uploadIdxList == null || uploadIdxList.isEmpty()) return;
		for (int uploadIdx : uploadIdxList) {
			if (!uploadService.isImageUploaded(uploadIdx)) {
				errors.rejectValue("boardContent", "NotUploadImage");
				return;
			}
		}
		
	}
}
