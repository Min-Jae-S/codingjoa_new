package com.codingjoa.service.test;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionStatus;
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
	
	private void checkTransaction(TransactionStatus status) {
		log.info("## checkTransaction");
		log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		for(Object key : TransactionSynchronizationManager.getResourceMap().keySet()) {
			ConnectionHolder connectionHolder = 
					(ConnectionHolder) TransactionSynchronizationManager.getResource(key);
			log.info("\t > conn = {}", connectionHolder.getConnection().toString().split(" ")[0]);
		}

		if (status.isCompleted()) {
			log.info("\t > status = Completed");
		} else if (status.isRollbackOnly()) {
			log.info("\t > status = Rollback");
		} else if (status.isNewTransaction()) {
			log.info("\t > status = New Transaction");
		} else {
			log.info("\t > status = Unknown");
		}
	}
	
	private TestVo createTestVo(String name) {
		log.info("## create testVo as '{}'", name);
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name(name)
				.password("test")
				.regdate(LocalDateTime.now())
				.build();
		log.info("\t > {}", testVo);
		return testVo;
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
	public void outer1() {
		log.info("## outer1");
		TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
		checkTransaction(status);
		
		// https://velog.io/@chullll/Transactional-%EA%B3%BC-PROXY
		// https://javafactory.tistory.com/1406
		// AOP(Proxy) self-invocation issue
		//this.inner1(); 
		service2.innerRequired();
		
		log.info("## outer1 - after calling innerRequired");
		checkTransaction(status);
	}

	@Transactional
	public void outer2(boolean rollback) {
		log.info("## outer2");
		log.info("\t > rollback = {}", rollback);
		TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
		checkTransaction(status);

		if (rollback) {
			mapper.insert(createTestVo("rollback"));
			service2.innerRollback();
			log.info("## outer2 - after calling innerRollback");
		} else {
			mapper.insert(createTestVo("commit"));
		}
			
		checkTransaction(status);
	}
	
	public void outer3() {
		log.info("## outer3");
		TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
		checkTransaction(status);
		service2.innerMandatory();
		
		log.info("## outer3 - after calling innerMandatory");
		checkTransaction(status);
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
