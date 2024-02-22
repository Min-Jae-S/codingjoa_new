package com.codingjoa.controller.test;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestJdbcService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestJdbcController {
	
	@Autowired
	private TestJdbcService jdbcService;
	
	@GetMapping("/jdbc/test1")
	public ResponseEntity<Object> jdbcTest1() throws ClassNotFoundException, SQLException { 
		log.info("## jdbcTest1");
		jdbcService.basicJdbc();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/jdbc/test2")
	public ResponseEntity<Object> jdbcTest2() { 
		log.info("## jdbcTest2");
		return ResponseEntity.ok("success");
	}
	
}
