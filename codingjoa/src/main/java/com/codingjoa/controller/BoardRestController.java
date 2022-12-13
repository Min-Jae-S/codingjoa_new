package com.codingjoa.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.error.SuccessResponse;

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
	public ResponseEntity<Object> uploadImage(@ModelAttribute @Valid UploadFileDto uploadFileDto, 
			BindingResult bindingResult) {
		
		MultipartFile file = uploadFileDto.getFile();
		log.info("originalFilename = {}", file.getOriginalFilename());
		log.info("contentType = {}", file.getContentType());
		
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(ObjectError -> {
				log.info("{}", ObjectError.getCodes()[0]);
			});
		}
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.uploadImage"));
	}
	
}
