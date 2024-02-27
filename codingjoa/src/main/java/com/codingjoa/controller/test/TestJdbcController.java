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
	
	@GetMapping("/jdbc/jdbc-basic")
	public ResponseEntity<Object> jdbcBasic() throws ClassNotFoundException, SQLException { 
		log.info("## jdbcBasic");
		jdbcService.jdbcBasic();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/jdbc/jdbc-template")
	public ResponseEntity<Object> jdbcTemplate() { 
		log.info("## jdbcTemplate");
		return ResponseEntity.ok("success");
	}
	
}
