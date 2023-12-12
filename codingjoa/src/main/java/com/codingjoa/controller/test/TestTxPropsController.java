package com.codingjoa.controller.test;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.service.test.TestTxPropsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestTxPropsController {
	
	@SuppressWarnings("unused")
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private TestTxPropsService service;
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;

	@GetMapping("/tx-props")
	public String main() {
		log.info("## main");
		return "test/tx-props";
	}
	
	@ResponseBody
	@GetMapping("/tx-props/propagation/test1")
	// parent: REQUIRED, child: REQUIRES_NEW
	public ResponseEntity<Object> propagationTest1() { 
		service.parent1(); 
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/propagation/test2")
	// parent: REQUIRED, child: NESTED
	public ResponseEntity<Object> propagationTest2() { 
		service.parent2(); 
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/propagation/test3")
	public ResponseEntity<Object> propagationTest3() { 
		// ...
		return ResponseEntity.ok("success");
	}
}
