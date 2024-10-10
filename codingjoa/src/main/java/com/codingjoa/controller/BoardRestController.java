package com.codingjoa.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.BoardImageDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/board")
@RequiredArgsConstructor
@RestController
public class BoardRestController {
	
	private final ImageService imageService;
	
	@PostMapping("/image")
	public ResponseEntity<Object> uploadBoardImage(@ModelAttribute @Valid UploadFileDto uploadFileDto)
			throws IllegalStateException, IOException {
		log.info("## uploadBoardImage");
		BoardImageDto boardImage = imageService.saveBoardImage(uploadFileDto.getFile());
	
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.messageByCode("success.UploadBoardImage")
				.data(boardImage)
				.build());
	}

}
