package com.codingjoa.controller;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestTxController {
	
	/*
	 * @ ApplicationContext.getBeansOfType -> BeanFactoryUtils.beansOfTypeIncludingAncestors
	 * 
	 * https://github.com/spring-projects/spring-framework/issues/15553
	 * Calling ApplicationContext.getBeansOfType(Class) intentionally does not consider the parent hierarchy (see the java doc). 
	 * You can use the BeanFactoryUtils class if you want to search the full hierarchy.
	 */
	
	@Autowired
	private ApplicationContext context;
	
	@GetMapping("/tx")
	public String main() {
		log.info("## tx main");
		return "test/tx";
	}
	
	@ResponseBody
	@GetMapping("/tx/txManagers")
	public ResponseEntity<Object> txManagers() {
		log.info("## txManagers");
		log.info("\t > txManagers from beansOfTypeIncludingAncestors = {}", 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PlatformTransactionManager.class).keySet());
		log.info("\t > txManagers from getBeansOfType = {}", 
				context.getBeansOfType(PlatformTransactionManager.class).keySet());
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/dataSources")
	public ResponseEntity<Object> dataSources() {
		log.info("## dataSources");
		log.info("\t > dataSources from beansOfTypeIncludingAncestors = {}", 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DataSource.class).keySet());
		log.info("\t > dataSources from getBeansOfType = {}", context.getBeansOfType(DataSource.class).keySet());
		return ResponseEntity.ok("success");
	}
	
}
