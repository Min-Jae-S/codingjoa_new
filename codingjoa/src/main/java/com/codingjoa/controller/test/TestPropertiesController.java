package com.codingjoa.controller.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@PropertySource("/WEB-INF/properties/test.properties")
@RequestMapping("/test/properties")
@RestController
public class TestPropertiesController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer;
	
	@Autowired
	private ResourcePatternResolver resourcePatternResolver;
	
	@Value("${test.param1}")
	private String param1;
	
	private final String param2;
	
	public TestPropertiesController(@Value("${test.param2}") String param2) {
		this.param2 = param2;
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() throws Exception {
		log.info("## test1");
		log.info("\t > param1 from env = {}", env.getProperty("test.param1"));
		log.info("\t > param1 from @value = {}", param1);
		log.info("\t > param2 from @value = {}", param2);
		
		PropertySources propertySources = propertySourcesPlaceholderConfigurer.getAppliedPropertySources();
		log.info("\t > propertySources = {}",
				(propertySources == null) ? null : propertySources.getClass().getName());
		
		propertySources.forEach(source -> log.info("\t > source = {}", source.getName()));
		
		Resource[] resources = resourcePatternResolver.getResources("WEB-INF/properties/*.properties");
		List<String> resourceFileNames = Arrays.stream(resources)
				.map(resource -> resource.getFilename())
				.collect(Collectors.toList());
		log.info("\t > resourceFileNames = {}", resourceFileNames);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
