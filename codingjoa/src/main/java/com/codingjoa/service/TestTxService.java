package com.codingjoa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxService {
	
	public void doSomething1() {
		log.info("## doSomething1");
		doSomething3();
	}
	
	@Transactional
	public void doSomething2() {
		log.info("## doSomething2");
		doSomething3();
	}

	@Transactional
	public void doSomething3() {
		log.info("## doSomething3");
		TransactionStatus status = null;
		try {
			status = TransactionAspectSupport.currentTransactionStatus();
		} catch (NoTransactionException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			log.info("\t > {}", status == null ? "no transcation" : "active transcation");
		}
	}
}
