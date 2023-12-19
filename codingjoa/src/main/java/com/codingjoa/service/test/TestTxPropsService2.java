package com.codingjoa.service.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxPropsService2 {
	
	private void chceckTransaction() {
		log.info("## chceckTransaction");
		log.info("\t > Transaction ID = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
		if (status.isCompleted()) {
			log.info("\t > Transaction status = COMPLETED");
		} else if (status.isRollbackOnly()) {
			log.info("\t > Transaction status = ROLLBACK");
		} else if (status.isNewTransaction()) {
			log.info("\t > Transaction status = NEW TRANSACTION");
		} else {
			log.info("\t > Transaction status = UNKNOWN");
		}
	}
	
	@Transactional
	public void inner1() {
		log.info("## inner1");
		chceckTransaction();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void inner2() {
		log.info("## inner2");
		chceckTransaction();
	}
}
