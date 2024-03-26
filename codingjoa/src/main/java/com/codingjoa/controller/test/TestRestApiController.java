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
	public ResponseEntity<Object> test2(HttpServletRequest request, RequestEntity entity) { 
		log.info("## test2");
//		log.info("\t ================ REQUEST ================");
//		log.info("\t > {}  {}  {}", request.getMethod(), request.getRequestURI(), request.getProtocol());
//		HttpHeaders headers = entity.getHeaders();
//		log.info("\t > Host: {}", headers.getHost());
//		log.info("\t > Accept: {}", headers.getAccept());
//		log.info("\t > Connection: {}", headers.getConnection());
		return ResponseEntity.ok("success");
	}

	@GetMapping("/rest-api/get")
	public ResponseEntity<Object> get() { 
		log.info("## get");
		return ResponseEntity.ok("success");
	}

	@PostMapping("/rest-api/post")
	public ResponseEntity<Object> post() { 
		log.info("## post");
		return ResponseEntity.ok("success");
	}

	@PutMapping("/rest-api/put")
	public ResponseEntity<Object> put() { 
		log.info("## put");
		return ResponseEntity.ok("success");
	}

	@PatchMapping("/rest-api/patch")
	public ResponseEntity<Object> patch() { 
		log.info("## patch");
		return ResponseEntity.ok("success");
	}

	@DeleteMapping("/rest-api/delete")
	public ResponseEntity<Object> delete() { 
		log.info("## delete");
		return ResponseEntity.ok("success");
	}
	
}
