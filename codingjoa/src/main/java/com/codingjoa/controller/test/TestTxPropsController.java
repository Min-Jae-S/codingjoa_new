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
import com.codingjoa.service.test.TestTxService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequestMapping("/test")
@Controller
public class TestTxPropsController {
	
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
	@GetMapping("/tx-props/propagation1")
	public ResponseEntity<Object> propagation1() { 
		service.doSomething1();
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/propagation2")
	public ResponseEntity<Object> propagation2() { 
		service.doSomething2();
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/propagation3")
	public ResponseEntity<Object> propagation3() { 
		service.doSomething3();
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/test4")
	public ResponseEntity<Object> test4() { 
		log.info("## test4");
		return ResponseEntity.ok("success");
	}
	
}
