package com.codingjoa.service.test;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.TestMapper1;
import com.codingjoa.mapper.TestMapper2;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Service
public class TestTxPropsService2 {
	
	@Autowired
	private TestMapper2 mapper2;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	private void checkTransaction() {
		try {
			TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
			log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
//			for(Object key : TransactionSynchronizationManager.getResourceMap().keySet()) {
//				ConnectionHolder connectionHolder = 
//						(ConnectionHolder) TransactionSynchronizationManager.getResource(key);
//				log.info("\t > conn = {}", connectionHolder.getConnection().toString().split(" ")[0]);
//			}
			String status = null;
			if (transactionStatus.isCompleted()) {
				status = "completed";
			} else if (transactionStatus.isRollbackOnly()) {
				status = "rollback";
			} else if (transactionStatus.isNewTransaction()) {
				status = "new transaction";
			} else {
				status = "unknown";
			}
			log.info("\t > transaction status = {}", status);
		} catch (Exception e) {
			log.info("\t > NO transaction = {}", e.getClass().getSimpleName());
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
	
	@Transactional
	public void innerRequired() {
		log.info("## innerRequired");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper2.insert(createTestVo("test2.innerRequired"));
		log.info("\t > inserted rows = {}", result);
		throw new RuntimeException("innerRequired");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerRequiresNew1() {
		log.info("## innerRequiresNew1");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper2.insert(createTestVo("test2.innerRequiresNew1"));
		log.info("\t > inserted rows = {}", result);
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerRequiresNew2() {
		log.info("## innerRequiresNew2");
		checkTransaction();
		log.info("\t > insert testVo");
		int result = mapper2.insert(createTestVo("test2.innerRequiresNew2"));
		log.info("\t > inserted rows = {}", result);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void innerMandatory() {
		log.info("## innerMandatory");
		checkTransaction();
	}
}
