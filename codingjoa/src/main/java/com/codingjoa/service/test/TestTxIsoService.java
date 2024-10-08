package com.codingjoa.service.test;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.test.TestIsoMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Service
public class TestTxIsoService {
	
	/*
	 * @@ CannotCreateTransactionException
	 * 	Could not open JDBC Connection for transaction; 	
	 * 	nested exception is java.sql.SQLException: READ_COMMITTED와 SERIALIZABLE만이 적합한 트랜잭션 레벨입니다
	 * 
	 * @@ Isolation Level
	 * 	Isolation level refers to the degree of isolation between transactions, 
	 * 	indicating how much transactions are isolated from each other.
	 *  In essence, it represents the intensity of access that one transaction has to data modified by other transactions.
	 *  As the isolation level increases, the degree of isolation between transactions also increases, 
	 *  often leading to performance degradation.
	 * 
	 * 	- DEFAULT ( Oracle: READ_COMMITTED, MySQL: REPEATABLE_READ )
	 *  - READ_UNCOMMITTED, READ_COMMITTED(*), REPEATABLE_READ, SERIALIZABLE(*)
	 *  
	 *  - READ_COMMITTED	( DIRTY READ: X,  NON-REPEATABLE READ: O,  PHANTOM READ: O )
	 * 		Access to changes is possible for other transactions only for committed modifications within the transaction.
	 *  	This is the isolation level commonly used by most RDBMS systems by default.
	 *  	A Shared Lock is applied during the execution of a SELECT statement. 
	 *  	Therefore, during the query, it retrieves the record values backed up in the Undo area rather than the actual table values.
	 *  	However, when the same SELECT query is executed within a single transaction, 
	 *  	it violates the consistency of REPEATABLE READ, where the same result should always be obtained. 
	 *  	In other words, a Non-repeatable Read occurs.
	 *  
	 *  - REPEATABLE_READ 	( DIRTY READ: X,  NON-REPEATABLE READ: X,  PHANTOM READ: O )
	 *  	Only committed content before the transaction starts is available for querying. 
	 *  	This is the default in MySQL, and in this isolation level, Non-repeatable Reads do not occur. 
	 *  	Until the transaction is completed, a Shared Lock is applied to all data used by the SELECT statement. 
	 *  	Therefore, it ensures that the content of the data queried within the transaction scope is always consistent
	 *  	Since the consistency of the data at the start of the transaction must be ensured, 
	 *  	a drawback arises as the transaction's execution time lengthens, requiring continued management of multi-versions.
	 *  	However, Phantom Reads can occur.
	 *  
	 *  - SERIALIZABLE	 	( DIRTY READ: X,  NON-REPEATABLE READ: X,  PHANTOM READ: X )
	 *  	It is the simplest yet most stringent isolation level, but in terms of performance, it has the lowest concurrency. 
	 *  	In SERIALIZABLE, PHANTOM READs do not occur. 
	 *  	Transactions happen sequentially rather than concurrently.
	 *  	However, it is rarely used.
	 *  
	 *  - DIRTY READ
	 *  	reading uncommitted data from another transaction
	 *  
	 *  - NON-REPEATABLE READ
	 *  	Inconsistency in values despite reading the same data in a single transaction
	 *  	typically occurring when data has been modified or deleted.
	 *  
	 *  - PHANTOM READ
	 *  	This is referred to as using only the data read within one's own transaction, 
	 *  	regardless of data committed by other transactions. 
	 *  	It describes a situation where, within a single transaction, 
	 *  	previously non-existent results are retrieved when the same query is executed multiple times. 
	 *  	This typically occurs when data insertion takes place.
	 */
	
	private CountDownLatch latchReadCommitted;
	private CountDownLatch latchSerializable;
	private CountDownLatch latchDefault;
	private boolean latchWaitingReadCommitted = false; 
	private boolean latchWaitingSerializable = false; 
	private boolean latchWaitingDefault = false; 

	@Autowired
	private TestIsoMapper isoMapper;
	
	@Autowired
	private TestTxService txService;
	
	private void checkTrasnaction() {
		log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName()); 				// @Nullable
		log.info("\t > isolation level = {}", TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()); // @Nullable
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void isoReadCommitted() {
		log.info("## Isolation.READ_COMMITTED [ {} ]", Thread.currentThread().getName());
		
		// first current number read within the transaction
		Integer initialCurrentNumber = isoMapper.findCurrentNumber();
		log.info("\t > initial current number = {}", initialCurrentNumber);
		try {
			log.info("\t > pause transaction");
			latchWaitingReadCommitted = true;
			latchReadCommitted = new CountDownLatch(1);
			latchReadCommitted.await();
		} catch (InterruptedException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
			Thread.currentThread().interrupt();
		}
		// second current number read within the same transaction
		Integer updatedCurrentNumber = isoMapper.findCurrentNumber();
		log.info("\t > next current number = {}", updatedCurrentNumber);
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void isoSerializable() {
		log.info("## Isolation.SERIALIZABLE");
		
		Integer initialCurrentNumber = isoMapper.findCurrentNumber();
		log.info("\t > initial current number = {}", initialCurrentNumber);
		try {
			log.info("\t > pause transaction  [ {} ]", Thread.currentThread().getName());
			latchWaitingSerializable = true;
			latchSerializable = new CountDownLatch(1);
			latchSerializable.await();
		} catch (InterruptedException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
			Thread.currentThread().interrupt();
		}
		Integer updatedCurrentNumber = isoMapper.findCurrentNumber();
		log.info("\t > next current number = {}", updatedCurrentNumber);
	}
	
	@Transactional(isolation = Isolation.DEFAULT)
	public void isoDefault() {
		log.info("## Isolation.DEFAULT");
		
		Integer initialCurrentNumber = isoMapper.findCurrentNumber();
		log.info("\t > initial current number = {}", initialCurrentNumber);
		try {
			log.info("\t > pause transaction");
			latchWaitingDefault = true;
			latchDefault = new CountDownLatch(1);
			latchDefault.await();
		} catch (InterruptedException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
			Thread.currentThread().interrupt();
		}
		Integer updatedCurrentNumber = isoMapper.findCurrentNumber();
		log.info("\t > next current number = {}", updatedCurrentNumber);
	}
	
	public void resumeReadCommitted(String option) {
		log.info("==========================================================================================");
		log.info("## resumeReadCommitted [ {} ]", Thread.currentThread().getName());
		if (latchWaitingReadCommitted) {
			if (option.equals("U")) {
				txService.updateCurrentNumber();
			} else if (option.equals("I")) {
				txService.insertRandomNumber();
			}
			log.info("\t > resume read-committed transaction");
			latchWaitingReadCommitted = false;
			latchReadCommitted.countDown();
		} else {
			log.info("\t > read-committed latch is not awaiting");
		}
		log.info("==========================================================================================");
	}
	
	public void resumeSerializable(String option) {
		log.info("==========================================================================================");
		log.info("## resumeSerializable [ {} ]", Thread.currentThread().getName());
		if (latchWaitingSerializable) {
			if (option.equals("U")) {
				txService.updateCurrentNumber();
			} else if (option.equals("I")) {
				txService.insertRandomNumber();
			}
			log.info("\t > resume serializable transaction");
			latchWaitingSerializable = false;
			latchSerializable.countDown();
		} else {
			log.info("\t > serializable latch is not awaiting");
		}
		log.info("==========================================================================================");
	}

	public void resumeDefault(String option) {
		log.info("==========================================================================================");
		log.info("## resumeDefault [ {} ]", Thread.currentThread().getName());
		if (latchWaitingDefault) {
			if (option.equals("U")) {
				txService.updateCurrentNumber();
			} else if (option.equals("I")) {
				txService.insertRandomNumber();
			}
			log.info("\t > resume default transaction");
			latchWaitingDefault = false;
			latchDefault.countDown();
		} else {
			log.info("\t > default latch is not awaiting");
		}
		log.info("==========================================================================================");
	}
}
