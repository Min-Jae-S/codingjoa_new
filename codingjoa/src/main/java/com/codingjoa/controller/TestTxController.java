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
	@GetMapping("/tx/txManager")
	public ResponseEntity<Object> txManager() {
		log.info("## txManager");
//		Map<String, PlatformTransactionManager> map = 
//				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PlatformTransactionManager.class);
//		for (String key : map.keySet()) {
//			log.info("\t > {} = {}", key, map.get(key));
//		}
		log.info("\t > txManager from beansOfTypeIncludingAncestors = {}", BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PlatformTransactionManager.class));
		log.info("\t > txManager from getBeansOfType = {}", context.getBeansOfType(PlatformTransactionManager.class));
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/dataSource")
	public ResponseEntity<Object> dataSource() {
		log.info("## dataSource");
		log.info("\t > dataSources from beansOfTypeIncludingAncestors = {}", BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DataSource.class));
		log.info("\t > dataSources from getBeansOfType = {}", context.getBeansOfType(DataSource.class));
		return ResponseEntity.ok("success");
	}
	
}
