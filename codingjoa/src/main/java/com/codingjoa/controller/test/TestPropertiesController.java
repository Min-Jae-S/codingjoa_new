package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("/WEB-INF/properties/test.properties")
@RequestMapping("/test/properties")
@RestController
public class TestPropertiesController {
	
	@Autowired
	private Environment env;
	
//	@Value("${test.param1}")
//	private String param1;
//	
//	private final String param2;
//	
//	public TestPropertiesController(@Value("${test.param2}") String param2) {
//		this.param2 = param2;
//	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() throws Exception {
		log.info("## test1");
		log.info("\t > param1 from env = {}", env.getProperty("test.param1"));
//		log.info("\t > param1 from @value = {}", param1);
//		log.info("\t > param2 = {}", param2);
		
		log.info("\t > classpath = {}", System.getProperty("java.class.path"));
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
