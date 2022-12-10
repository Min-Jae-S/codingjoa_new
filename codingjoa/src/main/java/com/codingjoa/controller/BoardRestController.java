package com.codingjoa.controller;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.UploadFileDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@RestController
public class BoardRestController {
	
	@PostMapping("/uploadImage")
	public String uploadImage(@ModelAttribute @Valid UploadFileDto uploadFileDto, 
			BindingResult bindingResult) {
		MultipartFile file = uploadFileDto.getFile();
		log.info("originalFilename = {}", file.getOriginalFilename());
		log.info("contentType = {}", file.getContentType());
		
		if(bindingResult.hasErrors()) {
			log.info("error");
		}
		
		return "";
	}
	
}
