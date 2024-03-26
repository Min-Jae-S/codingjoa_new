package com.codingjoa.controller.test;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestRestApiService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings(value = {"unused", "rawtypes"})
@Slf4j
@RequestMapping("/test")
@RestController
public class TestRestApiController {
	
	@Autowired
	private TestRestApiService service;

	@GetMapping("/rest-api/test1")
	public ResponseEntity<Object> test1(@RequestHeader Map<String, String> headers) { 
		log.info("## test1");
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			log.info("\t > {}: {}", entry.getKey(), entry.getValue());
		}
		return ResponseEntity.ok("success");
	}

	@GetMapping("/rest-api/test2")
	public ResponseEntity<Object> test2(HttpServletRequest request, RequestEntity entity) { 
		log.info("## test2");
		log.info("\t > {}  {}  {}", request.getMethod(), request.getRequestURI(), request.getProtocol());
		HttpHeaders headers = entity.getHeaders();
		log.info("\t > Host: {}", headers.getHost());
		log.info("\t > Accept: {}", headers.getAccept());
		log.info("\t > Connection: {}", headers.getConnection());
		return ResponseEntity.ok("success");
	}
	
}
