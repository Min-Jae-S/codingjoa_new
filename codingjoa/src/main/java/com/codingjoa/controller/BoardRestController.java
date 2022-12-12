package com.codingjoa.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.UploadFileDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@RestController
public class BoardRestController {
	
	@Resource(name = "uploadFileValidator")
	private Validator uploadFileValidator;
	
	@InitBinder("uploadFileDto")
	public void initBinderJoin(WebDataBinder binder) {
		binder.addValidators(uploadFileValidator);
	}
	
	@PostMapping("/uploadImage")
	public String uploadImage(@ModelAttribute @Valid UploadFileDto uploadFileDto, 
			BindingResult bindingResult) {
		uploadFileDto.getFile().forEach(file -> {
			log.info("originalFilename = {}", file.getOriginalFilename());
			log.info("contentType = {}", file.getContentType());
		});
		
		return "";
	}
	
}
