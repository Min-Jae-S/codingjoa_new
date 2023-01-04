package com.codingjoa.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.ModifyBoardDto;
import com.codingjoa.dto.WriteBoardDto;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "modifyBoardValidator")
public class ModifyBoardValidator implements Validator {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ModifyBoardDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== ModifyBoardValidator validate ==============");
		
		ModifyBoardDto modifyBoardDto = (ModifyBoardDto) target;
		
		if (!categoryService.isBoardCategory(modifyBoardDto.getBoardCategoryCode())) {
			errors.rejectValue("boardCategoryCode", "NotBoard");
			return;
		}
		
		if (!StringUtils.hasText(modifyBoardDto.getBoardTitle())) {
			errors.rejectValue("boardTitle", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(modifyBoardDto.getBoardContent())) {
			errors.rejectValue("boardContent", "NotBlank");
			return;
		}
		
		List<Integer> uploadIdxList = modifyBoardDto.getUploadIdxList();
		
		if (uploadIdxList != null) {
			for (int uploadIdx : uploadIdxList) {
				if (!boardService.isTempImageUploaded(uploadIdx)) {
					errors.rejectValue("boardContent", "NotTempImage");
					return;
				}
			}
		}
		
		
		
		
		
	}

}
