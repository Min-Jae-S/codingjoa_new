package com.codingjoa.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/board")
public class BoardRestController {
	
	@Value("${upload.board.path}")
	private String boardPath;

	// When using @PathVariable to capture a portion of the URL path as a variable, the dot (.) character is excluded by default. 
	// The dot (.) is considered a character that represents a file extension and is therefore not included in path variables.
	@GetMapping("/images/{boardImageName:.+}") 
	public ResponseEntity<Resource> getBoardImageResource(@PathVariable String boardImageName) throws MalformedURLException {
		log.info("## getBoardImageResource");
		log.info("\t > boardImageName = {}", boardImageName);
		
		// 절대경로를 db에서 가져오기
		String boardImagePath = boardPath + boardImageName; 
		log.info("\t > boardImagePath = {}", boardImagePath);
		
		return ResponseEntity.ok(new UrlResource("file:" + boardImagePath));
	}
}
