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
@SuppressWarnings("unused")
@Service
public class TestTxPropsService2 {
	
	@Autowired
	private TestMapper mapper;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	private void chceckTransaction() {
		log.info("## chceckTransaction");
		try {
			TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
			log.info("\t > Current Transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
			if (status.isCompleted()) {
				log.info("\t > Completed");
			} else if (status.isRollbackOnly()) {
				log.info("\t > Rollback");
			} else if (status.isNewTransaction()) {
				log.info("\t > New Transaction");
			} else {
				log.info("\t > Suspending or Unknown");
			}
		} catch (Exception e) {
			log.info("\t > No Transaction : {}", e.getClass().getSimpleName());
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

	@Transactional(propagation = Propagation.MANDATORY)
	public void inner3() {
		log.info("## inner3");
		chceckTransaction();
	}
}
