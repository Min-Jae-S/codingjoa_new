package com.codingjoa.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

	@GetMapping("/images/{boardImageName}")
	public Resource getBoardImageResource(@PathVariable String boardImageName) throws MalformedURLException {
		log.info("## getBoardImageResource");
		log.info("\t > boardImageName = {}", boardImageName);
		
		return new UrlResource("file:" + boardPath + boardImageName);
	}
}
