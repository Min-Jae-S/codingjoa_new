package com.codingjoa.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestTxController {
	
	@Autowired // WebApplicationContext = ApplicationContext + getServletContext()
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@GetMapping("/tx")
	public String main() {
		log.info("## tx main");
		return "test/tx";
	}
	
	@ResponseBody
	@GetMapping("/tx/config")
	public ResponseEntity<Object> config() {
		log.info("## tx config");
		Map<String, PlatformTransactionManager> txManagerMap = 
				webApplicationContext.getBeansOfType(PlatformTransactionManager.class);
		Map<String, DataSourceTransactionManager> txManagerMap2 = 
				webApplicationContext.getBeansOfType(DataSourceTransactionManager.class);
		log.info("\t > txManagerMap = {}", txManagerMap);
		log.info("\t > txManagerMap2 = {}", txManagerMap2);
		log.info("\t > txManagers from context = {}", txManagerMap.keySet());
		log.info("\t > txManagers from autowired = {}", transactionManager);
		return ResponseEntity.ok("success");
	}

}
