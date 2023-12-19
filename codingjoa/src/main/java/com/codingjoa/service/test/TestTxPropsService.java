package com.codingjoa.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import com.codingjoa.mapper.test.TestMapper;

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
	private PlatformTransactionManager txManager;
	
	@Autowired
	private TestTxPropsService2 service2;
	
	@Autowired
	private TestMapper mapper;
	
	private void chceckTransaction() {
		log.info("## chceckTransaction");
		log.info("\t > Current Transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
		if (status.isCompleted()) {
			log.info("\t > Completed");
		} else if (status.isRollbackOnly()) {
			log.info("\t > Rollback");
		} else if (status.isNewTransaction()) {
			log.info("\t > New Transaction");
		} else {
			log.info("\t > Suspending or Unknown");
		}
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
		chceckTransaction();
		
		// AOP(Proxy) self-invocation issue
		//this.inner1(); 
		service2.inner1();
	}

	@Transactional
	public void outer2() {
		log.info("## outer2");
		chceckTransaction();
		service2.inner2();
	}
	
	public void outer3() {
		log.info("## outer3");
		chceckTransaction();
		service2.inner1();
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
