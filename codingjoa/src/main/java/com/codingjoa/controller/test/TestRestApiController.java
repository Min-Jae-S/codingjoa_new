package com.codingjoa.controller.test;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
//		for (Map.Entry<String, String> entry : headers.entrySet()) {
//			log.info("\t > {}: {}", entry.getKey(), entry.getValue());
//		}
		return ResponseEntity.ok("success");
	}

	@GetMapping("/rest-api/test2")
	public ResponseEntity<Object> test2(HttpServletRequest request, RequestEntity requestEntity) { 
		log.info("## test2");
//		log.info("\t ================ REQUEST ================");
//		log.info("\t > {}  {}  {}", request.getMethod(), request.getRequestURI(), request.getProtocol());
//		HttpHeaders headers = requestEntity.getHeaders();
//		log.info("\t > Host: {}", headers.getHost());
//		log.info("\t > Accept: {}", headers.getAccept());
//		log.info("\t > Connection: {}", headers.getConnection());
		return ResponseEntity.ok("success");
	}

	@GetMapping("/rest-api/test3-numbers/{idx}")
	public ResponseEntity<Object> get(@PathVariable Integer idx, RequestEntity requestEntity) { 
		log.info("## get");
		log.info("\t > idx = {}", idx);
		return ResponseEntity.ok("success");
	}

	@PostMapping("/rest-api/test3-numbers")
	public ResponseEntity<Object> post(RequestEntity requestEntity) { 
		log.info("## post");
		return ResponseEntity.ok("success");
	}

	@PutMapping("/rest-api/test3-numbers/{idx}")
	public ResponseEntity<Object> put(@PathVariable Integer idx, RequestEntity requestEntity) { 
		log.info("## put");
		log.info("\t > idx = {}", idx);
		return ResponseEntity.ok("success");
	}

	@PatchMapping("/rest-api/test3-numbers/{idx}")
	public ResponseEntity<Object> patch(@PathVariable Integer idx, RequestEntity requestEntity) { 
		log.info("## patch");
		log.info("\t > idx = {}", idx);
		return ResponseEntity.ok("success");
	}

	@DeleteMapping("/rest-api/test3-numbers/{idx}")
	public ResponseEntity<Object> delete(@PathVariable Integer idx, RequestEntity requestEntity) { 
		log.info("## delete");
		log.info("\t > idx = {}", idx);
		return ResponseEntity.ok("success");
	}
	
}
