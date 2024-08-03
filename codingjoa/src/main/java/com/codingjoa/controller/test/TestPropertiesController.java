package com.codingjoa.controller.test;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySources;
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
	private ResourcePatternResolver resourcePatternResolver;
	
	@Autowired
	private PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer;
	
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
		
		log.info("\t > resourcePatternResolver = {}",
				(resourcePatternResolver == null) ? null : resourcePatternResolver.getClass().getName());
		
		PropertySources propertySources = propertySourcesPlaceholderConfigurer.getAppliedPropertySources();
		log.info("\t > propertySources = {}",
				(propertySources == null) ? null : propertySources.getClass().getName());
		
		propertySources.forEach(source -> {
			if (source instanceof PropertiesPropertySource) {
				PropertiesPropertySource propertiesPropertySource = (PropertiesPropertySource) source;
				Map<String, Object> map = propertiesPropertySource.getSource();
				if (map.isEmpty()) {
					log.info("\t > no properties");
				} else {
					log.info("\t > property = {}", map.keySet());
				}
			}
		});
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
