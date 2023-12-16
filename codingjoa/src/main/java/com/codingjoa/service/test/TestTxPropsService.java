package com.codingjoa.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.test.TestMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	@SuppressWarnings("unused")
	@Autowired
	private PlatformTransactionManager txManager;
	
	@SuppressWarnings("unused")
	@Autowired
	private TestMapper mapper;
	
	public void chceckTransaction() {
		log.info("## checkTransaction");
		log.info("\t > current transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
		if (status.isCompleted()) {
			log.info("\t > COMPLETED");
		} else if (status.isRollbackOnly()) {
			log.info("\t > ROLLBACK");
		} else if (status.isNewTransaction()) {
			log.info("\t > NEW TRANSACTION");
		} else {
			log.info("\t > UNKNOWN");
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void parent1() {
		log.info("## parent1 - REQUIRED");
		chceckTransaction();
		log.info("\t > calling child1...");
		child1();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void child1() {
		log.info("## child1 - REQUIRES_NEW");
		chceckTransaction();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void parent2() {
		log.info("## parent2 - REQUIRED");
		chceckTransaction();
		log.info("\t > calling child2...");
		child2();
	}
	
	@Transactional(propagation = Propagation.NESTED)
	public void child2() {
		log.info("## child2 - NESTED");
		chceckTransaction();
	}
}
