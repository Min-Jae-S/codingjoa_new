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

	/*
	 * JDBC (Java Database Connectivity) is a programming API in Java that provides a standardized way to access databases. 
	 * The JDBC API is declared as mostly abstract interfaces, allowing it to be used regardless of the type of DBMS.
	 * Each DBMS company provides JDBC drivers by bundling together class files that implement these interfaces.
	 */
	
	@Autowired
	private TestJdbcService jdbcService;
	
	@GetMapping("/jdbc/jdbc-basic")
	public ResponseEntity<Object> jdbcBasic() throws ClassNotFoundException, SQLException { 
		log.info("## jdbcBasic");
		jdbcService.jdbcBasic();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/jdbc/jdbc-datasource")
	public ResponseEntity<Object> jdbcDataSource() throws SQLException { 
		log.info("## jdbcDataSource");
		jdbcService.jdbcDataSource();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/jdbc/spring-jdbc")
	public ResponseEntity<Object> springJdbc() { 
		log.info("## springJdbc");
		jdbcService.springJdbc();
		return ResponseEntity.ok("success");
	}
	
}
