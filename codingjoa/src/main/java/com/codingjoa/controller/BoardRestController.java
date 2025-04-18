package com.codingjoa.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.BoardImageDto;
import com.codingjoa.dto.ImageFileDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.ImageService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "Board Image API")
@Slf4j
@RequestMapping("/api/board")
@RequiredArgsConstructor
@RestController
public class BoardRestController {
	
	private final ImageService imageService;
	
	@PostMapping("/image")
	public ResponseEntity<Object> saveImageWithUpload(@ModelAttribute @Valid ImageFileDto imageFileDto)
			throws IllegalStateException, IOException {
		log.info("## saveImageWithUpload");
		
		BoardImageDto boardImage = imageService.saveBoardImageWithUpload(imageFileDto.getFile());
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.messageByCode("success.board.saveImageWithUpload")
				.data(boardImage)
				.build());
	}

}
