package com.codingjoa.controller.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestJdbcService;
import com.codingjoa.test.TestItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestJdbcController {

	/*
	 * JDBC (Java Database Connectivity) is a programming API in Java that provides a standardized way to access databases. 
	 * The JDBC API is declared as mostly abstract interfaces, allowing it to be used regardless of the type of DBMS.
	 * Each DBMS company provides JDBC drivers by bundling together class files that implement these interfaces.
	 */
	
	@Autowired
	private TestJdbcService jdbcService;
	
	@GetMapping("/jdbc/driver-manager")
	public ResponseEntity<Object> useDriverManager() { 
		log.info("## useDriverManager");
		jdbcService.useDriverManager();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/jdbc/data-source")
	public ResponseEntity<Object> useDataSource() { 
		log.info("## useDataSource");
		jdbcService.useDataSource();
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/jdbc/test-items") 
	public ResponseEntity<Object> findTestItems() { 
		log.info("## findTestItems");
		List<TestItem> testItems = jdbcService.findTestItems();
		return ResponseEntity.ok(testItems);
	}

	@GetMapping("/jdbc/jdbc-template")
	public ResponseEntity<Object> useJdbcTemplate() { 
		log.info("## springJdbc");
		jdbcService.useJdbcTemplate();
		return ResponseEntity.ok("success");
	}
	
}
