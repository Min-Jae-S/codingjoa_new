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
		log.info("\t > transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName()); // @Nullable 
		try {
//			for(Object key : TransactionSynchronizationManager.getResourceMap().keySet()) {
//				ConnectionHolder connectionHolder = 
//						(ConnectionHolder) TransactionSynchronizationManager.getResource(key);
//				log.info("\t > conn = {}", connectionHolder.getConnection().toString().split(" ")[0]);
//			}
			TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
			if (status.isNewTransaction()) {
				log.info("\t > new transaction");
			} else {
				log.info("\t > not new transaction");
			}
		} catch (Exception e) {
			log.info("\t > no transaction - {}", e.getClass().getSimpleName());
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
		mapper2.insert(createTestVo("test2.innerRequired"));
		throw new RuntimeException("innerRequired");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerRequiresNew1() {
		log.info("## innerRequiresNew1");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper2.insert(createTestVo("test2.innerRequiresNew1"));
		throw new RuntimeException("innerRequiresNew1");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerRequiresNew2() {
		log.info("## innerRequiresNew2");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper2.insert(createTestVo("test2.innerRequiresNew2"));
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void innerMandatory() {
		log.info("## innerMandatory");
		checkTransaction();
	}
	
	@Transactional(propagation = Propagation.NESTED)
	public void innerNested1() {
		log.info("## innerNested1");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper2.insert(createTestVo("test2.innerRequiresNew2"));
		throw new RuntimeException("innerNested1");
	}

	@Transactional(propagation = Propagation.NESTED)
	public void innerNested2() {
		log.info("## innerNested2");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper2.insert(createTestVo("test2.innerRequiresNew2"));
	}
}
