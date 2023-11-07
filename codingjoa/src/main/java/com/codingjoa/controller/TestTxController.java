package com.codingjoa.controller;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.config.DataSourceConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestTxController {
	
	@Autowired
	private ApplicationContext context;
	
	@GetMapping("/tx")
	public String main() {
		log.info("## tx main");
		return "test/tx";
	}
	
	@ResponseBody
	@GetMapping("/tx/config")
	public ResponseEntity<Object> config() {
		log.info("## tx config");
		log.info("\t > txManager from getBeansOfType = {}", context.getBeansOfType(PlatformTransactionManager.class));
		log.info("\t > txManager from getBean = {}", context.getBean(PlatformTransactionManager.class));
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/dataSource")
	public ResponseEntity<Object> dataSource() {
		log.info("## tx dataSource");
		log.info("\t > dataSource from getBeansOfType = {}", context.getBeansOfType(DataSource.class));
		log.info("\t > dataSource from getBean = {}", context.getBean(DataSource.class));
//		log.info("\t > dataSource from getBeansOfType(annotationConfigContext) = {}", annotationConfigContext.getBeansOfType(DataSource.class));
//		log.info("\t > dataSource from getBean(annotationConfigContext) = {}", annotationConfigContext.getBean(DataSource.class));
		log.info("\t > dataSources");
		Map<String, DataSource> dataSources = context.getBeansOfType(DataSource.class);
		for (String key : dataSources.keySet()) {
			DataSource dataSource = dataSources.get(key);
			log.info("\t    - key = {}, value = {}", key, dataSource);
		}
		return ResponseEntity.ok("success");
	}
	
}
