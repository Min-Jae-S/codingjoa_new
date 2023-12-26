package com.codingjoa.controller.test;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestTxPropsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@RequestMapping("/test")
@RestController
public class TestTxPropsController {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private TestTxPropsService service;
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;
	
	@GetMapping("/tx-props/rollback/test1")
	public ResponseEntity<Object> rollbackTest1() { 
		log.info("## rollbackTest1");
		service.rollback1();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test2")
	public ResponseEntity<Object> rollbackTest2() throws Exception { 
		log.info("## rollbackTest2");
		service.rollback2();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test3")
	public ResponseEntity<Object> rollbackTest3() throws Exception { 
		log.info("## rollbackTest3");
		service.rollbackForException();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test4")
	public ResponseEntity<Object> rollbackTest4() throws Exception { 
		log.info("## rollbackTest4");
		service.rollbackForSqlException();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test1")
	public ResponseEntity<Object> propagationTest1() { 
		log.info("## propagationTest1");
		
		// outer: REQUIRED, inner: REQUIRED
		service.outer1();
		
		// Creating new transaction with name [outer1]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Participating in existing transaction
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/propagation/test2")
	public ResponseEntity<Object> propagationTest2() { 
		log.info("## propagationTest2");
		
		// outer: REQUIRED, inner: REQUIRED_NEW(Rollback)
		service.outer2(true); 
		
		// Creating new transaction with name [outer2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Suspending current transaction, creating new transaction with name [inner2]
		// Resuming suspended transaction after completion of inner transaction
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/propagation/test3")
	public ResponseEntity<Object> propagationTest3() { 
		log.info("## propagationTest3");
		
		// outer: REQUIRED, inner: REQUIRED_NEW(Commit)
		service.outer2(false);
		
		// Creating new transaction with name [outer2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Suspending current transaction, creating new transaction with name [inner2]
		// Resuming suspended transaction after completion of inner transaction
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/propagation/test4")
	public ResponseEntity<Object> propagationTest4() { 
		log.info("## propagationTest4");
		// outer: NO TX, inner: MANDATORY
		service.outer3();
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/isolation/test1")
	public ResponseEntity<Object> isolationTest1() { 
		log.info("## isolationTest1");
		service.isoDefault();
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/isolation/test2")
	public ResponseEntity<Object> isolationTest2() {
		log.info("## isolationTest2");
		service.isoReadCommitted();
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/isolation/test3")
	public ResponseEntity<Object> isolationTest3() { 
		log.info("## isolationTest3");
		service.isoReadUncommitted();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/isolation/test4")
	public ResponseEntity<Object> isolationTest4() { 
		log.info("## isolationTest4");
		service.isoRepeatableRead();
		return ResponseEntity.ok("success");
	}
}
