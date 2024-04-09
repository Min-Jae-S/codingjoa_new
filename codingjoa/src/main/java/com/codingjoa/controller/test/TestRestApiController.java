package com.codingjoa.controller.test;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.service.test.TestRestApiService;
import com.codingjoa.test.TestApiRequestData;
import com.codingjoa.test.TestApiResponseData;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings(value = "rawtypes")
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
		List<TestApiResponseData> responseData = service.read();
		log.info("\t > response = {}", responseData);
		return ResponseEntity.ok(SuccessResponse.builder().data(responseData));
	}
	
	@GetMapping(value = { "/rest-api/test-members/", "/rest-api/test-members/{id}" })
	public ResponseEntity<Object> getMapping2(@PathVariable String id) { 
		log.info("## getMapping2");
		log.info("\t > id = {}", id);
		TestApiResponseData responseData = service.readById(id);
		log.info("\t > response = {}", responseData);
		return ResponseEntity.ok(SuccessResponse.builder().data(responseData));
	}

	@PostMapping("/rest-api/test-members")
	public ResponseEntity<Object> postMapping(/* @RequestBody TestApiRequestData requestData */) { 
		log.info("## postMapping");
		//log.info("\t > request = {}", requestData);
		service.create();
		return ResponseEntity.ok("success");
	}

	// @@ REST(Representational State Transfer)
	// 자원을 이름(자원의 표현)으로 구분하여 해당 자원의 상태(정보)를 주고받는 것을 의미한다.
	// REST는 자원 기반의 구조(ROA, Resource Oriented Architecture) 설계의 중심에 Resource가 있고 
	// HTTP Method를 통해 Resource를 처리하도록 설계된 아키텍쳐를 의미한다.
	// REST는 HTTP URI(Uniform Resource Identifier)를 통해 자원(Resource)을 명시하고, 
	// HTTP Method(POST, GET, PUT, DELETE)를 통해 해당 자원에 대한 CRUD Operation을 적용하는 것을 의미한다.
	
	// @@ PUT(update, idempotent) - PATCH(partial update, not idempotent - append)
	
	// @@ idempotent method
	// 동일한 요청을 한번 보내는 것과 여러번 연속으로 보내는 것이 같은 효과를 가지고, 서버의 상태도 동일하게 남을 때 idempotent라고 한다.
	// A method that ensures the same resource state is returned for the same request, regardless of how many times it is made.
	// Sometimes, there's a misconception that idempotent methods always return the same response. 
	// However, if the server resource state remains the same, the method is idempotent regardless of the response being different.
	
	// @@ I/O error while reading input message; nested exception is java.io.IOException: Stream closed"
	// Your error is the result of @RequestBody being used twice(or being used RequestEntity) in your controller method arguments.
	// Using @RequestBody Spring converts incoming request body into the specified object (what closes the stream representing body at the end) 
	// so attempting to use @RequestBody second time in the same method makes no sense as stream has been already closed.
	@PutMapping("/rest-api/test-members/{id}")
	public ResponseEntity<Object> putMapping(@PathVariable String id, @RequestBody TestApiRequestData requestData) { 
		log.info("## putMapping");
		log.info("\t > id = {}", id);
		log.info("\t > request = {}", requestData);
		
		int result = service.update(requestData, id);
		if (result > 0) {
			TestApiResponseData responseData = service.readById(id);
			log.info("\t > response = {}", responseData);
			return ResponseEntity
					.ok()
					.body(SuccessResponse.builder().data(responseData).build());
		} else {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(ErrorResponse.builder().status(HttpStatus.NOT_FOUND).build());
		}
	}

	@PatchMapping("/rest-api/test-members/{id}")
	public ResponseEntity<Object> patchMapping(@PathVariable String id , @RequestBody TestApiRequestData requestData) { 
		log.info("## patchMapping");
		log.info("\t > id = {}", id);
		log.info("\t > request = {}", requestData);
		
		int result = service.update(requestData, id);
		if (result > 0) {
			TestApiResponseData responseData = service.readById(id);
			log.info("\t > response = {}", responseData);
			return ResponseEntity
					.ok()
					.body(SuccessResponse.builder().data(responseData).build());
		} else {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(ErrorResponse.builder().status(HttpStatus.NOT_FOUND).build());
		}
	}

	@DeleteMapping("/rest-api/test-members/{id}")
	public ResponseEntity<Object> deleteMapping(@PathVariable String id) { 
		log.info("## deleteMapping");
		log.info("\t > id = {}", id);
		int result = service.delete(id);
		if (result > 0) {
			return ResponseEntity
					.status(HttpStatus.NO_CONTENT)
					.body(SuccessResponse.builder().status(HttpStatus.NO_CONTENT).build());
		} else {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(ErrorResponse.builder().status(HttpStatus.NOT_FOUND).build());
		}
	}
	
}
