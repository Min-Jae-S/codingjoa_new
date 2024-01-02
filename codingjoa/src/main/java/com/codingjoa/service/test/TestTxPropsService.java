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

import com.codingjoa.mapper.TestMapper;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Service
public class TestTxPropsService {
	
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
	private TestMapper mapper;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	private void checkTransaction() {
		log.info("## checkTransaction");
		try {
			TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
			log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
//			for(Object key : TransactionSynchronizationManager.getResourceMap().keySet()) {
//				ConnectionHolder connectionHolder = 
//						(ConnectionHolder) TransactionSynchronizationManager.getResource(key);
//				log.info("\t > conn = {}", connectionHolder.getConnection().toString().split(" ")[0]);
//			}

			if (status.isCompleted()) {
				log.info("\t > Completed ");
			} else if (status.isRollbackOnly()) {
				log.info("\t > Rollback ");
			} else if (status.isNewTransaction()) {
				log.info("\t > New Transaction");
			} else {
				log.info("\t > Unknown");
			}
		} catch (Exception e) {
			log.info("\t > NO transaction - {}", e.getClass().getSimpleName());
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
	
	// Committing JDBC transaction on Connection
	@Transactional
	public void rollback1() { 
		log.info("## rollback1");
		checkTransaction();
		try {
			log.info("## rollback1 - insert testVo");
			mapper.insert(createTestVo("rollback1"));
			throw new RuntimeException();
		} catch (Exception e) {
			log.info("## rollback1 - catch {}", e.getClass().getSimpleName());
		}
	}

	// Rolling back JDBC transaction on Connection
	@Transactional
	public void rollback2() { 
		log.info("## rollback2");
		checkTransaction();
		log.info("## rollback2 - insert testVo");
		mapper.insert(createTestVo("rollback2"));
		throw new RuntimeException("rollback2");
	}
	
	// Rolling back JDBC transaction on Connection
	@Transactional(rollbackFor = Exception.class)
	public void rollbackForException() throws Exception { 
		log.info("## rollbackForEx");
		checkTransaction();
		log.info("## rollbackForEx - insert testVo");
		mapper.insert(createTestVo("rollbackForEx"));
		throw new SQLException("rollbackForEx");
	}

	// Rolling back JDBC transaction on Connection
	@Transactional(rollbackFor = SQLException.class)
	public void rollbackForSqlException() throws Exception {
		log.info("## rollbackForSqlEx");
		checkTransaction();
		log.info("## rollbackForSqlEx - insert testVo");
		mapper.insert(createTestVo("rollbackForSqlEx"));
		throw new SQLException("rollbackForSqlEx");
	}

	// Committing JDBC transaction on Connection
	@Transactional
	public void noRollbackForSqlException() throws Exception { 
		log.info("## noRollbackForSqlEx");
		checkTransaction();
		log.info("## noRollbackForSqlEx - insert testVo");
		mapper.insert(createTestVo("noRollbackForSqlEx"));
		throw new SQLException("noRollbackForSqlEx");
	}

	// Committing JDBC transaction on Connection
	@Transactional
	public void checkedException() throws Exception { 
		log.info("## checkedEx");
		checkTransaction();
		log.info("## checkedEx - insert testVo");
		mapper.insert(createTestVo("checkedEx"));
		throw new IOException("checkedEx");
	}

	// Rolling back JDBC transaction on Connection
	@Transactional
	public void uncheckedException() { 
		log.info("## uncheckedEx");
		checkTransaction();
		log.info("## uncheckedEx - insert testVo");
		mapper.insert(createTestVo("uncheckedEx"));
		throw new RuntimeException("uncheckedEx");
	}
	
	@Transactional
	public void outer1() {
		log.info("## outer1");
		checkTransaction();
		
		mapper.insert(createTestVo("outer1"));
		// https://velog.io/@chullll/Transactional-%EA%B3%BC-PROXY
		// https://javafactory.tistory.com/1406
		// AOP(Proxy) self-invocation issue
		//this.inner1(); 
		
		// throw RuntimeException
		service2.innerRequired();
		
		log.info("## outer1 - after calling innerRequired");
		checkTransaction();
	}

	@Transactional
	public void outer2(boolean rollback) {
		log.info("## outer2");
		checkTransaction();

		log.info("## outer2 - insert testVo");
		if (rollback) {
			mapper.insert(createTestVo("rollback"));
			service2.innerRollback();
			log.info("## outer2 - after calling innerRollback");
		} else {
			mapper.insert(createTestVo("noRollback"));
			service2.innerNoRollback();
			log.info("## outer2 - after calling innerNoRollback");
		}
		checkTransaction();
	}
	
	public void outer3() {
		log.info("## outer3");
		checkTransaction();
		service2.innerMandatory();
		
		log.info("## outer3 - after calling innerMandatory");
		checkTransaction();
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
