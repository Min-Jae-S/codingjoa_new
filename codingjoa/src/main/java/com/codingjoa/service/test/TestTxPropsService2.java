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
	
	private void checkTransaction() {
		log.info("## checkTransaction");
		try {
			TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
			log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
			
//			for(Object key : TransactionSynchronizationManager.getResourceMap().keySet()) {
//				ConnectionHolder connectionHolder = 
//						(ConnectionHolder) TransactionSynchronizationManager.getResource(key);
//				log.info("\t > conn = {}", connectionHolder.getConnection().toString().split(" ")[0]);
//			}

			if (status.isCompleted()) {
				log.info("\t > Completed");
			} else if (status.isRollbackOnly()) {
				log.info("\t > Rollback");
			} else if (status.isNewTransaction()) {
				log.info("\t > New Transaction");
			} else {
				log.info("\t > Unknown");
			}
		} catch (Exception e) {
			log.info("\t > NO transaction = {}", e.getClass().getSimpleName());
		}
	}
	
	private TestVo createTestVo(String name) {
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name(name)
				.password("test")
				.regdate(LocalDateTime.now())
				.build();
		return testVo;
	}
	
	@Transactional
	public void innerRequired() {
		log.info("## innerRequired");
		checkTransaction();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerRollback() {
		log.info("## innerRollback");
		checkTransaction();
		
		try {
			log.info("## innerRollback - insert testVo");
			mapper.insert(createTestVo("innerRollback"));
			throw new SQLException();
		} catch (SQLException e) {
			log.info("## innerRollback - catch {}", e.getClass().getSimpleName());
			checkTransaction();
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerCommit() {
		log.info("## innerCommit");
		checkTransaction();
		
		log.info("## innerCommit - insert testVo");
		mapper.insert(createTestVo("innerCommit"));
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void innerMandatory() {
		log.info("## innerMandatory");
		checkTransaction();
	}
}
