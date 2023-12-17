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
	public ResponseEntity<Object> propagationTest1() { 
		log.info("## propagationTest1");
		service.parent1(); // parent: REQUIRED, child: REQUIRES_NEW
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/propagation/test2")
	public ResponseEntity<Object> propagationTest2() { 
		log.info("## propagationTest2");
		service.parent2(); // parent: REQUIRED, child: NESTED
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/propagation/test3")
	public ResponseEntity<Object> propagationTest3() { 
		log.info("## propagationTest3");
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/isolation/test1")
	public ResponseEntity<Object> isolationTest1() { 
		log.info("## isolationTest1");
		service.isoDefault();
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/isolation/test2")
	public ResponseEntity<Object> isolationTest2() {
		log.info("## isolationTest2");
		service.isoReadCommitted();
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx-props/isolation/test3")
	public ResponseEntity<Object> isolationTest3() { 
		log.info("## isolationTest3");
		service.isoReadUncommitted();
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx-props/isolation/test4")
	public ResponseEntity<Object> isolationTest4() { 
		log.info("## isolationTest4");
		service.isoRepeatableRead();
		return ResponseEntity.ok("success");
	}
}
