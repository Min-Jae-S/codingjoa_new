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

import com.codingjoa.service.TestTxService;

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
	
	@Autowired
	private TestTxService testTxService;
	
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

	@ResponseBody
	@GetMapping("/tx/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		testTxService.doSomething1(); // doSomething1(NO @Transactional) -> doSomething3(@Transactional)
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		testTxService.doSomething2(); // doSomething2(@Transactional) -> doSomething3(@Transactional)
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		testTxService.doSomething3(); // doSomething3(@Transactional)
		return ResponseEntity.ok("success");
	}
	
}
