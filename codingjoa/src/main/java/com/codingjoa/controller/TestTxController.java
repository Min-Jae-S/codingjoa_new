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
	
	 // ApplicationContext.getBeansOfType -> BeanFactoryUtils.beansOfTypeIncludingAncestors
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
		log.info("\t > txManagers = {}", BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PlatformTransactionManager.class).keySet());
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/dataSource")
	public ResponseEntity<Object> dataSource() {
		log.info("## dataSource");
		log.info("\t > dataSources = {}", BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DataSource.class).keySet());
		return ResponseEntity.ok("success");
	}
	
}
