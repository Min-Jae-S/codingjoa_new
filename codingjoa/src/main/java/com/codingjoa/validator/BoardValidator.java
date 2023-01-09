package com.codingjoa.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.BoardDto;
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
		
		BoardDto boardDto = (BoardDto) target;
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
		
		String objectName = errors.getObjectName();
		
		if (objectName.equals("writeBoardDto")) {
			log.info("============== WriteBoardValidator validate ==============");
			
			List<Integer> uploadIdxList = boardDto.getUploadIdxList();
			
			if (uploadIdxList == null) return;
			
			for (int uploadIdx : uploadIdxList) {
				if (!boardService.isTempImageUploaded(uploadIdx)) {
					errors.rejectValue("boardContent", "NotTempImage");
					return;
				}
			}
		} else if (objectName.equals("modifyBoardDto")) {
			log.info("============== ModifyBoardValidator validate ==============");
		}
		
	}
}
