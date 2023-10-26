package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class H2Config {

	@Value("${datasource.h2.classname}")
	private String driverClassName;

	@Value("${datasource.h2.url}")
	private String url;
	
	@Value("${datasource.h2.username}")
	private String username;
	
	@Value("${datasource.h2.password}")
	private String password;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ H2Config");
		log.info("===============================================================");
	}
	
	
}
