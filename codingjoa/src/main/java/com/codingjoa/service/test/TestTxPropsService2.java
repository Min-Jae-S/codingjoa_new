package com.codingjoa.service.test;

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

import com.codingjoa.mapper.TestMapper;
import com.codingjoa.test.TestVo;

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
		} catch (Exception e) {
			log.info("\t > No Transaction, {}", e.getClass().getSimpleName());
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
