package com.codingjoa.controller.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequestMapping("/test/concurrency")
@RestController
public class TestConcurrencyController {

	@Qualifier("mainHikariConfig")
	@Autowired
	private HikariConfig hikariConfig;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		log.info("\t > connection timeout: {}", hikariConfig.getConnectionTimeout());
		log.info("\t > maximum pool size: {}", hikariConfig.getMaximumPoolSize());
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	
	
	
}
