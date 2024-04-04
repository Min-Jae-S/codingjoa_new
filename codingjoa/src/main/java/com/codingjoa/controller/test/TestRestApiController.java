package com.codingjoa.controller.test;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestRestApiService;
import com.codingjoa.test.TestMember;

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
//		log.info("\t > {}  {}  {}", request.getMethod(), request.getRequestURI(), request.getProtocol());
//		HttpHeaders headers = requestEntity.getHeaders();
//		log.info("\t > Host: {}", headers.getHost());
//		log.info("\t > Accept: {}", headers.getAccept());
//		log.info("\t > Connection: {}", headers.getConnection());
		return ResponseEntity.ok("success");
	}

	@GetMapping("/rest-api/test3")
	public ResponseEntity<Object> test3(HttpServletRequest request, RequestEntity requestEntity) { 
		log.info("## test3");
		return ResponseEntity.ok("success");
	}

	@GetMapping("/rest-api/test-members")
	public ResponseEntity<Object> getMapping() { 
		log.info("## getMapping");
		List<TestMember> testMembers = service.read();
		return ResponseEntity.ok(testMembers);
	}
	
	@GetMapping(value = { "/rest-api/test-members/", "/rest-api/test-members/{id}" })
	public ResponseEntity<Object> getMapping2(@PathVariable String id) { 
		log.info("## getMapping2");
		log.info("\t > id = {}", id);
		TestMember testMember = service.readById(id);
		return ResponseEntity.ok(testMember);
	}

	@PostMapping("/rest-api/test-members")
	public ResponseEntity<Object> postMapping(/* @RequestBody TestMember testMember */) { 
		log.info("## postMapping");
		//log.info("\t > member = {}", testMember);
		service.create();
		return ResponseEntity.ok("success");
	}

	// @@ I/O error while reading input message; nested exception is java.io.IOException: Stream closed"
	// Your error is the result of @RequestBody being used twice(or being used RequestEntity) in your controller method arguments.
	// Using @RequestBody Spring converts incoming request body into the specified object (what closes the stream representing body at the end) 
	// so attempting to use @RequestBody second time in the same method makes no sense as stream has been already closed.
	@PutMapping("/rest-api/test-members/{id}")
	public ResponseEntity<Object> putMapping(@PathVariable String id, @RequestBody TestMember testMember) { 
		log.info("## putMapping");
		log.info("\t > id = {}", id);
		log.info("\t > member = {}", testMember);
		service.update();
		return ResponseEntity.ok("success");
	}

	@PatchMapping("/rest-api/test-members/{id}")
	public ResponseEntity<Object> patchMapping(@PathVariable String id , @RequestBody TestMember testMember ) { 
		log.info("## patchMapping");
		log.info("\t > id = {}", id);
		log.info("\t > member = {}", testMember);
		service.update();
		return ResponseEntity.ok("success");
	}

	@DeleteMapping("/rest-api/test-members/{id}")
	public ResponseEntity<Object> deleteMapping(@PathVariable String id) { 
		log.info("## deleteMapping");
		log.info("\t > id = {}", id);
		service.delete();
		return ResponseEntity.ok("success");
	}
	
}
