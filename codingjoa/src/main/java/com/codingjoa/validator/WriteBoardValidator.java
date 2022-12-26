package com.codingjoa.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.WriteBoardDto;
import com.codingjoa.entity.Category;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "writeBoardValidator")
public class WriteBoardValidator implements Validator {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return WriteBoardDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== WriteBoardValidator validate ==============");
		
		WriteBoardDto writeBoardDto = (WriteBoardDto) target;
		
		Category category = categoryService.findCategory(writeBoardDto.getBoardCategoryCode());
		if (category == null) {
			errors.rejectValue("boardCategoryCode", "NotCategory");
			return;
		}
		
		Integer cateogyParentCode = category.getCategoryParentCode();
		
		if (cateogyParentCode == null) {
			errors.rejectValue("boardCategoryCode", "NotBoard");
			return;
		}
		
		if (cateogyParentCode != 1) {
			errors.rejectValue("boardCategoryCode", "NotBoard");
			return;
		}
		
		if (!StringUtils.hasText(writeBoardDto.getBoardTitle())) {
			errors.rejectValue("boardTitle", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(writeBoardDto.getBoardContent())) {
			errors.rejectValue("boardContent", "NotBlank");
			return;
		}
		
		List<Integer> uploadIdxList = writeBoardDto.getUploadIdxList();
		
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
