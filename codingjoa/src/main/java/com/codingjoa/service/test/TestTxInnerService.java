package com.codingjoa.service.test;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.test.TestInnerMapper;
import com.codingjoa.test.TestEvent;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxInnerService {
	
	@Autowired
	private TestInnerMapper innerMapper;
	
	@SuppressWarnings("unused")
	@Autowired
	private PlatformTransactionManager txManager;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
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
				log.info("\t > status = new transaction");
			} else {
				log.info("\t > status = unknown");
			}
		} catch (Exception e) {
			log.info("\t > status = no transaction - {}", e.getClass().getSimpleName());
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
	public void innerRequired1() {
		log.info("## innerRequired1");
		checkTransaction();

		TestVo testVo = createTestVo("innerRequired1");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));

		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		innerMapper.insert(testVo);
		
		log.info("\t > will throw RuntimeException in innerRequired1");
		throw new RuntimeException("innerRequired1");
	}

	@Transactional
	public void innerRequired2() {
		log.info("## innerRequired2");
		checkTransaction();
		
		TestVo testVo = createTestVo("innerRequired2");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		innerMapper.insert(testVo);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerRequiresNew1() {
		log.info("## innerRequiresNew1");
		checkTransaction();
		
		TestVo testVo = createTestVo("innerRequiresNew1");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		innerMapper.insert(testVo);
		
		log.info("\t > will throw RuntimeException in innerRequiresNew1");
		throw new RuntimeException("innerRequiresNew1");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void innerRequiresNew2() {
		log.info("## innerRequiresNew2");
		checkTransaction();
		
		TestVo testVo = createTestVo("innerRequiresNew2");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		innerMapper.insert(testVo);
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
		
		TestVo testVo = createTestVo("innerNested1");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		innerMapper.insert(testVo);
		
		log.info("\t > will throw RuntimeException in innerNested1");
		throw new RuntimeException("innerNested1");
	}

	@Transactional(propagation = Propagation.NESTED)
	public void innerNested2() {
		log.info("## innerNested2");
		checkTransaction();
		
		TestVo testVo = createTestVo("innerNested2");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		innerMapper.insert(testVo);
	}
}
