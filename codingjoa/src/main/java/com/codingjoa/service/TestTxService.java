package com.codingjoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.TestMapper;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxService {
	
	/*
	 * @@ DefaultTransactionDefinition implements TransactionDefinition
	 * 	- Transaction Propagation
	 * 	- Isolation Level
	 * 	- Timeout
	 * 	- Read Only
	 */
	
	@Autowired
	private TestMapper testMapper;
	
	public void doSomething1() {
		log.info("## TestTxService.doSomething1");
		doSomething3();
	}
	
	@Transactional
	public void doSomething2() {
		log.info("## TestTxService.doSomething2");
		doSomething3();
	}

	@Transactional
	public void doSomething3() {
		log.info("## TestTxService.doSomething3");
		TransactionStatus status = null;
		try {
			status = TransactionAspectSupport.currentTransactionStatus();
		} catch (NoTransactionException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			checkTransaction(status);
		}
	}

	@Transactional(value = "subTransactionManager")
	public void doSomething4() {
		log.info("## TestTxService.doSomething4");
		TransactionStatus status = null;
		try {
			status = TransactionAspectSupport.currentTransactionStatus();
		} catch (NoTransactionException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			checkTransaction(status);
		}
	}
	
	private void checkTransaction(TransactionStatus status) {
		log.info("## checkTransaction");
		log.info("\t > transaction name = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("\t > transaction status = {}", status);
		if (status == null) {
			log.info("\t > NO TRANSACTION");
		} else {
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
		
	}
	
	public List<TestVo> select() {
		log.info("## TestTxService.select");
		return testMapper.select();
	}
	
	public int insert(TestVo testVo) {
		log.info("## TestTxService.insert");
		return testMapper.insert(testVo);
	}
	
	public int update(TestVo testVo) {
		log.info("## TestTxService.update");
		return testMapper.update(testVo);
	}
	
	public int remove() {
		log.info("## TestTxService.remove");
		return testMapper.remove();
	}
	
}
