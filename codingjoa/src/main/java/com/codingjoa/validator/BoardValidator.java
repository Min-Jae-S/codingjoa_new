package com.codingjoa.validator;

import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.BoardDto;
import com.codingjoa.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BoardValidator implements Validator {

	private final ImageService imageService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return BoardDto.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		// boardIdx or boardCategoryCode could have errors - int boardIdx, @BoardCategoryCode
		if (errors.hasFieldErrors()) {
			return; 
		}

		BoardDto boardDto = (BoardDto) target;
		if (!StringUtils.hasText(boardDto.getBoardTitle())) {
			errors.rejectValue("boardTitle", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(boardDto.getBoardContent())) {
			errors.rejectValue("boardContent", "NotBlank");
			return;
		}
		
		List<Integer> boardImages = boardDto.getBoardImages();
		if (boardImages.isEmpty()) {
			return;
		}
		
		for (int boardImageIdx : boardImages) {
			if (!imageService.isBoardImageUploaded(boardImageIdx)) {
				errors.rejectValue("boardContent", "NotUploadImage");
				return;
			}
		}
		
	}
}
