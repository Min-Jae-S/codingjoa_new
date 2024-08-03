package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@PropertySource("/WEB-INF/properties/test.properties")
@Slf4j
@RequestMapping("/test/properties")
@RestController
public class TestPropertiesController {
	
	@Value("${test.param1}")
	private String param1;
	
	@Value("${test.param2}")
	private Integer param2;
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() throws Exception {
		log.info("## test1");
		log.info("\t > param1 = {}", param1);
		log.info("\t > param2 = {}", param2);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
