package com.codingjoa.controller.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestTxIsoService;
import com.codingjoa.service.test.TestTxOuterService;
import com.codingjoa.service.test.TestTxService;
import com.codingjoa.service.test.TestTxTimeOutService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@RequestMapping("/test")
@RestController
public class TestTxPropsController {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private TestTxOuterService outerService;
	
	@Autowired
	private TestTxService txService;
	
	@Autowired
	private TestTxIsoService isoService;
	
	@Autowired
	private TestTxTimeOutService timeoutService;
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;
	
	/*
	 * @@ ROLLBACK TEST
	 * 	- catch vs no catch
	 * 	- checked exception vs unchecked exception
	 */
	
	@GetMapping("/tx-props/rollback/test1")
	public ResponseEntity<Object> rollbackTest1() { 
		log.info("## rollbackTest1");
		outerService.rollback1();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test2")
	public ResponseEntity<Object> rollbackTest2() throws Exception { 
		log.info("## rollbackTest2");
		outerService.rollback2();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test3")
	public ResponseEntity<Object> rollbackTest3() throws Exception { 
		log.info("## rollbackTest3");
		outerService.rollbackForEx();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test4")
	public ResponseEntity<Object> rollbackTest4() throws Exception { 
		log.info("## rollbackTest4");
		outerService.rollbackForSqlEx();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test5")
	public ResponseEntity<Object> rollbackTest5() throws Exception { 
		log.info("## rollbackTest5");
		outerService.noRollbackForSqlEx();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test6")
	public ResponseEntity<Object> rollbackTest6() throws Exception { 
		log.info("## rollbackTest6");
		outerService.checkedEx();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/rollback/test7")
	public ResponseEntity<Object> rollbackTest7() { 
		log.info("## rollbackTest7");
		outerService.uncheckedEx();
		return ResponseEntity.ok("success");
	}
	
	/*
	 * @@ PROPAGATION TEST
	 * 	- outer = REQUIRED, 		inner = REQUIRED
	 * 	- outer = REQUIRED, 		inner = REQUIRED_NEW
	 * 	- outer = NO TRANSACTION, 	inner = MANDATORY
	 * 	- outer = REQUIRED, 		inner = NESTED
	 */

	@GetMapping("/tx-props/propagation/test1")
	public ResponseEntity<Object> propagationTest1() { 
		log.info("## propagationTest1");
		log.info("\t > outer = REQUIRED, inner = REQUIRED");
		
		// @@ outer: REQUIRED, inner: REQUIRED (inner Exception)
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
		outerService.outer1(); 
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/propagation/test2/{innerException}")
	public ResponseEntity<Object> propagationTest2(@PathVariable boolean innerException) { 
		log.info("## propagationTest2");
		log.info("\t > outer = REQUIRED, inner = REQUIRED_NEW");
		log.info("\t > innerException = {}", innerException);
		
		// @@ outer = REQUIRED, inner = REQUIRED_NEW (inner Exception with catch)
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
		
		// @@ outer = REQUIRED, inner = REQUIRED_NEW (NO Exception)
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
		outerService.outer2(innerException); 
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx-props/propagation/test3")
	public ResponseEntity<Object> propagationTest3() { 
		log.info("## propagationTest3");
		outerService.outer3();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test4")
	public ResponseEntity<Object> propagationTest4() { 
		log.info("## propagationTest3");
		// @@ outer = NO TRANSACTION, inner = MANDATORY
		outerService.outer4();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test5")
	public ResponseEntity<Object> propagationTest5() { 
		log.info("## propagationTest5");
		// @@ outer = REQUIRED, inner = NESTED (Exception at inner)
		// Creating new transaction with name [outer5]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Acquired Connection [HikariProxyConnection@1294584318] for JDBC transaction
		// Creating nested transaction with name [innerNested1]
		// Rolling back transaction to savepoint
		// Initiating transaction commit
		// Committing JDBC transaction on Connection [HikariProxyConnection@1294584318]
		// Releasing JDBC Connection [HikariProxyConnection@1294584318] after transaction
		outerService.outer5();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx-props/propagation/test6")
	public ResponseEntity<Object> propagationTest6() { 
		log.info("## propagationTest6");
		// @@ outer = REQUIRED, inner = NESTED (NO Exception)
		// Creating new transaction with name [outer6]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
		// Acquired Connection [HikariProxyConnection@1420424474] for JDBC transaction
		// Creating nested transaction with name [innerNested2]
		// Releasing transaction savepoint
		// Initiating transaction commit
		// Committing JDBC transaction on Connection [HikariProxyConnection@1420424474]
		// Releasing JDBC Connection [HikariProxyConnection@1420424474] after transaction
		outerService.outer6();
		return ResponseEntity.ok("success");
	}
	
	// @@ ISOLATION TEST
	@GetMapping("/tx-props/isolation/numbers")
	public ResponseEntity<Object> findNumbers() {
		log.info("## findNumbers");
		List<Integer> numbers = txService.findNumbers();
		if (numbers.size() > 0) {
			log.info("\t > numbers = {}", numbers);
		} else {
			log.info("\t > no numbers");
		}
		return ResponseEntity.ok(numbers);
	}

	@GetMapping("/tx-props/isolation/current-number")
	public ResponseEntity<Object> findCurrentNumber() {
		log.info("## findCurrentNumber");
		Integer number = txService.findCurrentNumber();
		if (number != null) {
			log.info("\t > current number = {}", number);
		} else {
			log.info("\t > no current number");
		}
		return ResponseEntity.ok(number);
	}
	
	@GetMapping("/tx-props/isolation/new")
	public ResponseEntity<Object> insertRandomNumber() {
		log.info("## insertRandomNumber");
		txService.insertRandomNumber();
		List<Integer> numbers = txService.findNumbers();
		if (numbers.size() > 0) {
			log.info("\t > numbers = {}", numbers);
		} else {
			log.info("\t > no numbers");
		}
		return ResponseEntity.ok(numbers);
	}
	
	@PostMapping("/tx-props/isolation/current-number")
	public ResponseEntity<Object> updateCurrentNumber() {
		log.info("## updateCurrentNumber");
		txService.updateCurrentNumber();
		Integer number = txService.findCurrentNumber();
		if (number != null) {
			log.info("\t > current number = {}", number);
		} else {
			log.info("\t > no current number");
		}
		return ResponseEntity.ok(number);
	}
	
	@DeleteMapping("/tx-props/isolation/numbers")
	public ResponseEntity<Object> deleteNumbers() {
		log.info("## deleteNumbers");
		txService.deleteNumbers();
		List<Integer> numbers = txService.findNumbers();
		if (numbers.size() > 0) {
			log.info("\t > numbers = {}", numbers);
		} else {
			log.info("\t > no numbers");
		}
		return ResponseEntity.ok(numbers);
	}
	
	@GetMapping("/tx-props/isolation/resume/read-committed/{option}")
	public ResponseEntity<Object> resumeReadCommitted(@PathVariable String option) {
		isoService.resumeReadCommitted(option);
		return ResponseEntity.ok("RESUME READ_COMMITTED success");
	}
	
	@GetMapping("/tx-props/isolation/resume/serializable/{option}")
	public ResponseEntity<Object> resumeSerializable(@PathVariable String option) {
		isoService.resumeSerializable(option);
		return ResponseEntity.ok("RESUME SERIALIZABLE success");
	}

	@GetMapping("/tx-props/isolation/resume/default/{option}")
	public ResponseEntity<Object> resumeDefault(@PathVariable String option) {
		isoService.resumeDefault(option);
		return ResponseEntity.ok("RESUME DEFAULT success");
	}
	
	@GetMapping("/tx-props/isolation/read-committed")
	public ResponseEntity<Object> isolationTest1() {
		log.info("## isolationTest1");
		isoService.isoReadCommitted();
		return ResponseEntity.ok("READ_COMMITTED success");
	}
	
	@GetMapping("/tx-props/isolation/serializable")
	public ResponseEntity<Object> isolationTest2() { 
		log.info("## isolationTest2");
		isoService.isoSerializable();
		return ResponseEntity.ok("SERIALIZABLE success");
	}
	
	@GetMapping("/tx-props/isolation/default")
	public ResponseEntity<Object> isolationTest3() {
		log.info("## isolationTest3");
		isoService.isoDefault();
		return ResponseEntity.ok("DEFAULT success");
	}
	
	// @@ TIME-OUT TEST
	@GetMapping("/tx-props/timeout/test1")
	public ResponseEntity<Object> timeoutTest1() {
		log.info("## timeoutTest1");
		timeoutService.invokeDelay();
		return ResponseEntity.ok("timeout success");
	} 
}
