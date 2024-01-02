package com.codingjoa.service.test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.TestMapper1;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Service
public class TestTxPropsService1 {
	
	/*
	 * @@ Transaction properties
	 * 	- DefaultTransactionDefinition implements TransactionDefinition
	 * 	- Transaction Propagation
	 * 	- Isolation Level
	 * 	- Timeout
	 * 	- Read Only
	 */
	
	@Autowired
	private TestTxPropsService2 service2;
	
	@Autowired
	private TestMapper1 mapper1;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	private void checkTransaction() {
		String status = null;
		try {
			TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
//			for(Object key : TransactionSynchronizationManager.getResourceMap().keySet()) {
//				ConnectionHolder connectionHolder = 
//						(ConnectionHolder) TransactionSynchronizationManager.getResource(key);
//				log.info("\t > conn = {}", connectionHolder.getConnection().toString().split(" ")[0]);
//			}
			if (transactionStatus.isCompleted()) {
				status = "completed";
			} else if (transactionStatus.isRollbackOnly()) {
				status = "rollback";
			} else if (transactionStatus.isNewTransaction()) {
				status = "new transaction";
			} else {
				status = "unknown";
			}
		} catch (Exception e) {
			status = "no transaction";
		} finally {
			log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
			log.info("\t > transaction status = {}", status);
		}
	}
	
	private TestVo createTestVo(String name) {
		return TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name(name)
				.password("test")
				.regdate(LocalDateTime.now())
				.build();
	}
	
	/* 
	 * @@ Propagation
	 * 	One of the advantages of declarative transactions provided by Spring, 
	 * 	specifically through transaction annotations such as @Transactional, 
	 * 	is the "ability to group multiple transactions together to create a larger transactional boundary"
	 * 	In the course of working, there are situations where additional transactions need to be performed 
	 * 	while an existing transaction is in progress. 
	 * 	Deciding how to proceed with an additional transaction when an existing one is already underway 
	 * 	is referred to as the propagation attribute.
	 */
	
	@Transactional
	public void rollback1() { 
		log.info("## rollback1");
		checkTransaction();
		try {
			log.info("\t > insert testVo");
			int result = mapper1.insert(createTestVo("rollback1"));
			log.info("\t > inserted rows = {}", result);
			throw new RuntimeException();
		} catch (Exception e) {
			log.info("> catches {}", e.getClass().getSimpleName());
		}
	}

	@Transactional
	public void rollback2() { 
		log.info("## rollback2");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("rollback2"));
		log.info("\t > inserted rows = {}", result);
		throw new RuntimeException("rollback2");
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void rollbackForException() throws Exception { 
		log.info("## rollbackForEx");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("rollbackForEx"));
		log.info("\t > inserted rows = {}", result);
		throw new SQLException("rollbackForEx");
	}

	@Transactional(rollbackFor = SQLException.class)
	public void rollbackForSqlException() throws Exception {
		log.info("## rollbackForSqlEx");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("rollbackForSqlEx"));
		log.info("\t > inserted rows = {}", result);
		throw new SQLException("rollbackForSqlEx");
	}

	@Transactional
	public void noRollbackForSqlException() throws Exception { 
		log.info("## noRollbackForSqlEx");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("noRollbackForSqlEx"));
		log.info("\t > inserted rows = {}", result);
		throw new SQLException("noRollbackForSqlEx");
	}

	@Transactional
	public void checkedException() throws Exception { 
		log.info("## checkedEx");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("checkedEx"));
		log.info("\t > inserted rows = {}", result);
		throw new IOException("checkedEx");
	}

	@Transactional
	public void uncheckedException() { 
		log.info("## uncheckedEx");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("uncheckedEx"));
		log.info("\t > inserted rows = {}", result);
		throw new RuntimeException("uncheckedEx");
	}
	
	@Transactional
	public void outer1() {
		log.info("## outer1");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("test1.outer1"));
		log.info("\t > inserted rows = {}", result);
		log.info("\t > calling innerRequired...");
		
		// https://velog.io/@chullll/Transactional-%EA%B3%BC-PROXY
		// https://javafactory.tistory.com/1406
		// AOP(Proxy) self-invocation issue
		//this.inner1();
		
		// REQUIRED vs REQUIRES_NEW
		//service2.innerRequired();
		try {
			service2.innerRequired();
		} catch (Exception e) {
			log.info("\t > catch {} at innerRequired", e.getClass().getSimpleName());
		}
	}

	@Transactional
	public void outer2(boolean rollback) {
		log.info("## outer2");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("test1.outer2"));
		log.info("\t > inserted rows = {}", result);

		if (rollback) {
			try {
				log.info("\t > calling innerRequiresNew1...");
				service2.innerRequiresNew1();
			} catch (Exception e) {
				log.info("\t > catch {} at innerRequiresNew1", e.getClass().getSimpleName());
			}
		} else {
			log.info("\t > calling innerRequiresNew2...");
			service2.innerRequiresNew2();
		}
	}
	
	@Transactional
	public void outer3() {
		log.info("## outer3");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("test1.outer3"));
		log.info("\t > inserted rows = {}", result);
		log.info("\t > calling innerRequiresNew2...");
		service2.innerRequiresNew2();
		throw new RuntimeException("outer3");
	}
	
	
	public void outer4() {
		log.info("## outer4");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("test1.outer4"));
		log.info("\t > inserted rows = {}", result);
		log.info("\t > calling innerMandatory...");
		service2.innerMandatory();
	}
	
	@Transactional
	public void outer5() {
		log.info("## outer5");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("test1.outer5"));
		log.info("\t > inserted rows = {}", result);
		log.info("\t > calling innerNested1...");
		service2.innerNested1();
	}

	@Transactional
	public void outer6() {
		log.info("## outer6");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper1.insert(createTestVo("test1.outer6"));
		log.info("\t > inserted rows = {}", result);
		log.info("\t > calling innerNested2...");
		service2.innerNested2();
	}

	@Transactional(isolation = Isolation.DEFAULT)
	public void isoDefault() {
		log.info("## Isolation.DEFAULT");
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void isoReadCommitted() {
		log.info("## Isolation.READ_COMMITTED");
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void isoReadUncommitted() {
		log.info("## Isolation.READ_UNCOMMITTED");
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void isoRepeatableRead() {
		log.info("## Isolation.REPEATABLE_READ");
	}
	
}
