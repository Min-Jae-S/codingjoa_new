package com.codingjoa.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.WriteBoardDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "writeBoardValidator")
public class WriteBoardValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return WriteBoardDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== WriteBoardValidator validate ==============");
		
		WriteBoardDto writeBoardDto = (WriteBoardDto) target;
		int boardCategoryCode = writeBoardDto.getBoardCategoryCode();
		List<Integer> uploadIdxList = writeBoardDto.getUploadIdxList();
		
		if (!StringUtils.hasText(writeBoardDto.getBoardTitle())) {
			errors.rejectValue("boardTitle", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(writeBoardDto.getBoardContent())) {
			errors.rejectValue("boardboardContent", "NotBlank");
			return;
		}
		
	}

}
