package com.codingjoa.controller.test;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestTxPropsService1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@RequestMapping("/test")
@RestController
public class TestTxPropsController {
	
	/*
	 * @@ @Transactional의 전파 레벨
	 * https://kth990303.tistory.com/385
	 */
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private TestTxPropsService1 service;
	
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

	@GetMapping("/tx-props/rollback/test5")
	public ResponseEntity<Object> rollbackTest5() throws Exception { 
		log.info("## rollbackTest5");
		service.noRollbackForSqlException();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test6")
	public ResponseEntity<Object> rollbackTest6() throws Exception { 
		log.info("## rollbackTest6");
		service.checkedException();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test7")
	public ResponseEntity<Object> rollbackTest7() { 
		log.info("## rollbackTest7");
		service.uncheckedException();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test1")
	public ResponseEntity<Object> propagationTest1() { 
		log.info("## propagationTest1");
		log.info("\t > outer = REQUIRED, inner = REQUIRED");
		
		// @@ outer: REQUIRED, inner: REQUIRED (RuntimeException)
		// @@ RuntimeException at inner
		// Creating new transaction with name [outer1]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Acquired Connection [HikariProxyConnection@742011473 wrapping oracle.jdbc.driver.T4CConnection@5ff37957] for JDBC transaction
		// Participating in existing transaction
		// Participating transaction failed - marking existing transaction as rollback-only
		// Setting JDBC transaction [HikariProxyConnection@742011473 wrapping oracle.jdbc.driver.T4CConnection@5ff37957] rollback-only
		// ** Global transaction is marked as rollback-only but transactional code requested commit --> UnexpectedRollbackException
		// Initiating transaction rollback
		// Rolling back JDBC transaction on Connection [HikariProxyConnection@742011473 wrapping oracle.jdbc.driver.T4CConnection@5ff37957]
		// Releasing JDBC Connection [HikariProxyConnection@742011473 wrapping oracle.jdbc.driver.T4CConnection@5ff37957] after transaction
		service.outer1(); 
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/propagation/test2/{innerException}")
	public ResponseEntity<Object> propagationTest2(@PathVariable boolean innerException) { 
		log.info("## propagationTest2");
		log.info("\t > outer = REQUIRED, inner = REQUIRED_NEW");
		log.info("\t > innerException = {}", innerException);
		
		// @@ outer = REQUIRED, inner = REQUIRED_NEW, inner Exception with catch
		// Creating new transaction with name [outer2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Acquired Connection [HikariProxyConnection@1991964660] for JDBC transaction
		// Suspending current transaction, creating new transaction with name [innerRollback]
		// Acquired Connection [HikariProxyConnection@2060061152] for JDBC transaction
		// Initiating transaction rollback
		// Rolling back JDBC transaction on Connection [HikariProxyConnection@2060061152]
		// Releasing JDBC Connection [HikariProxyConnection@2060061152] after transaction
		// Resuming suspended transaction after completion of inner transaction
		// Initiating transaction commit
		// Committing JDBC transaction on Connection [HikariProxyConnection@1991964660]
		// Releasing JDBC Connection [HikariProxyConnection@1991964660] after transaction
		
		// @@ outer = REQUIRED, inner = REQUIRED_NEW, NO Exception
		// Creating new transaction with name [outer2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Acquired Connection [HikariProxyConnection@1043314600] for JDBC transaction
		// Suspending current transaction, creating new transaction with name [innerNoRollback]
		// Acquired Connection [HikariProxyConnection@886562008]
		// Initiating transaction commit
		// Committing JDBC transaction on Connection [HikariProxyConnection@886562008]
		// Releasing JDBC Connection [HikariProxyConnection@886562008] after transaction
		// Resuming suspended transaction after completion of inner transaction
		// Initiating transaction commit
		// Committing JDBC transaction on Connection [HikariProxyConnection@1043314600]
		// Releasing JDBC Connection [HikariProxyConnection@1043314600] after transaction
		service.outer2(innerException); 
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/propagation/test3")
	public ResponseEntity<Object> propagationTest3() { 
		log.info("## propagationTest3");
		service.outer3();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test4")
	public ResponseEntity<Object> propagationTest4() { 
		log.info("## propagationTest3");
		// outer = NO TRANSACTION, inner = MANDATORY
		service.outer4();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test5")
	public ResponseEntity<Object> propagationTest5() { 
		log.info("## propagationTest5");
		// @@ outer = REQUIRED, inner = NESTED, RuntimeException at inner
		// Creating new transaction with name [outer5]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Acquired Connection [HikariProxyConnection@1294584318] for JDBC transaction
		// Creating nested transaction with name [innerNested1]
		// Rolling back transaction to savepoint
		// Initiating transaction commit
		// Committing JDBC transaction on Connection [HikariProxyConnection@1294584318]
		// Releasing JDBC Connection [HikariProxyConnection@1294584318] after transaction
		service.outer5();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test6")
	public ResponseEntity<Object> propagationTest6() { 
		log.info("## propagationTest6");
		// @@ outer = REQUIRED, inner = NESTED, NO RuntimeExeption
		// Creating new transaction with name [outer6]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Acquired Connection [HikariProxyConnection@1420424474] for JDBC transaction
		// Creating nested transaction with name [innerNested2]
		// Releasing transaction savepoint
		// Initiating transaction commit
		// Committing JDBC transaction on Connection [HikariProxyConnection@1420424474]
		// Releasing JDBC Connection [HikariProxyConnection@1420424474] after transaction
		service.outer6();
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
