package com.codingjoa.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@RestController
public class BoardRestController {
	
	@PostMapping("/uploadImage")
	public String uploadImage(MultipartFile upload) {
		log.info("{}", upload);
		
		// MultipartFile	// ??
		// MultipartRequest // cos.jar
		
		return "";
	}
	
}
