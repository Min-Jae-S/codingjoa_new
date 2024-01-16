package com.codingjoa.service.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestIsoService {
	
	// @@ CannotCreateTransactionException
	// Could not open JDBC Connection for transaction; 
	// nested exception is java.sql.SQLException: READ_COMMITTED와 SERIALIZABLE만이 적합한 트랜잭션 레벨입니다
	
	/*
	 * @@ Isolation Level
	 * 	- DEFAULT ( Oracle: READ_COMMITTED, MySQL: REPEATABLE_READ )
	 *  - READ_UNCOMMITTED, READ_COMMITTED(*), REPEATABLE_READ, SERIALIZABLE(*)
	 *  - DIRTY READ, NON REPEATABLE READ, PHANTOM READ
	 */
	
	private void checkTrasnactionInfo() {
		log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName()); // @Nullable
		log.info("\t > isolation level = {}", TransactionSynchronizationManager.getCurrentTransactionIsolationLevel());
	}
	
	@Transactional(isolation = Isolation.DEFAULT)
	public void isoDefault() {
		log.info("## Isolation.DEFAULT");
		checkTrasnactionInfo();
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void isoReadCommitted() {
		log.info("## Isolation.READ_COMMITTED");
		checkTrasnactionInfo();
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void isoSerializable() {
		log.info("## Isolation.SERIALIZABLE");
		checkTrasnactionInfo();
	}
	
}
