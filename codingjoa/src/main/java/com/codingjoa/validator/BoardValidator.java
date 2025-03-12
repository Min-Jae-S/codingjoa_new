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

		// boardId or categoryCode could have errors - long boardId, @BoardCategoryCode
		if (errors.hasFieldErrors()) {
			return; 
		}

		BoardDto boardDto = (BoardDto) target;
		if (!StringUtils.hasText(boardDto.getTitle())) {
			errors.rejectValue("title", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(boardDto.getContent())) {
			errors.rejectValue("content", "NotBlank");
			return;
		}
		
		List<Long> boardImages = boardDto.getImages();
		if (boardImages.isEmpty()) {
			return;
		}
		
		for (long boardImageId : boardImages) {
			if (!imageService.isBoardImageUploaded(boardImageId)) {
				errors.rejectValue("content", "NotUploadImage");
				return;
			}
		}
		
	}
}
