package com.codingjoa.service.test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.TestMapper1;
import com.codingjoa.test.TestEvent;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxPropsService1 {
	
	/*
	 * @@ Transaction properties
	 * 	- DefaultTransactionDefinition implements TransactionDefinition
	 * 	- Transaction Propagation
	 * 	- Isolation Level
	 * 	- Timeout
	 * 	- Read Only
	 */
	
	@Autowired
	private TestTxPropsService2 service2;
	
	@Autowired
	private TestMapper1 mapper1;
	
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
	
	/* 
	 * @@ Propagation
	 * 	One of the advantages of declarative transactions provided by Spring, 
	 * 	specifically through transaction annotations such as @Transactional, 
	 * 	is the "ability to group multiple transactions together to create a larger transactional boundary"
	 * 	In the course of working, there are situations where additional transactions need to be performed 
	 * 	while an existing transaction is in progress. 
	 * 	Deciding how to proceed with an additional transaction when an existing one is already underway 
	 * 	is referred to as the propagation attribute.
	 */
	
	@Transactional
	public void rollback1() { 
		log.info("## rollback1");
		checkTransaction();
		TestVo testVo = createTestVo("rollback1");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		try {
			log.info("\t > insert testVo ( name = {} )", testVo.getName());
			mapper1.insert(testVo);
			
			log.info("\t > throw RuntimeException in rollback1");
			throw new RuntimeException();
		} catch (Exception e) {
			log.info("\t > catch {}", e.getClass().getSimpleName());
		}
	}

	@Transactional
	public void rollback2() { 
		log.info("## rollback2");
		checkTransaction();
		TestVo testVo = createTestVo("rollback2");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		mapper1.insert(testVo);
		
		log.info("\t > throw RuntimeException in rollback2");
		throw new RuntimeException("rollback2");
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void rollbackForException() throws Exception { 
		log.info("## rollbackForEx");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper1.insert(createTestVo("rollbackForEx"));
		throw new SQLException("rollbackForEx");
	}

	@Transactional(rollbackFor = SQLException.class)
	public void rollbackForSqlException() throws Exception {
		log.info("## rollbackForSqlEx");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper1.insert(createTestVo("rollbackForSqlEx"));
		throw new SQLException("rollbackForSqlEx");
	}

	@Transactional
	public void noRollbackForSqlException() throws Exception { 
		log.info("## noRollbackForSqlEx");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper1.insert(createTestVo("noRollbackForSqlEx"));
		throw new SQLException("noRollbackForSqlEx");
	}

	@Transactional
	public void checkedException() throws Exception { 
		log.info("## checkedEx");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper1.insert(createTestVo("checkedEx"));
		throw new IOException("checkedEx");
	}

	@Transactional
	public void uncheckedException() { 
		log.info("## uncheckedEx");
		checkTransaction();
		log.info("\t > insert testVo");
		mapper1.insert(createTestVo("uncheckedEx"));
		throw new RuntimeException("uncheckedEx");
	}
	
	@Transactional
	public void outer1() {
		log.info("## outer1");
		checkTransaction();
		TestVo testVo = createTestVo("test1.outer1");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		mapper1.insert(testVo);
		
		// AOP(Proxy) self-invocation issue
		// https://velog.io/@chullll/Transactional-%EA%B3%BC-PROXY
		// https://javafactory.tistory.com/1406
		// https://whitepro.tistory.com/581
		// https://www.popit.kr/%EB%8F%99%EC%9D%BC%ED%95%9C-beanclass%EC%97%90%EC%84%9C-transactional-%EB%8F%99%EC%9E%91-%EB%B0%A9%EC%8B%9D/
		// @Transactional 어노테이션이 Spring의 CGLIB Proxy를 기반으로 동작하기 때문이다. 
		// 다시 말해 동일한 Bean으로 등록된 클래스의 메서드에서는 @Transactional을 단일 건으로 취급한다. 
		// Proxy로 불러온 빈은 다른 클래스가 아닌 경우 인터셉트되어 전달되지 않기 때문에 @Transactional이 동작하지 않는다.
		//this.inner1();
		
		// REQUIRED vs REQUIRES_NEW
		//service2.innerRequired();
		
		// outer: rollback, inner: rollback
		log.info("\t > calling innerRequired...");
		service2.innerRequired();
	}

	@Transactional
	public void outer2(boolean innerException) {
		log.info("## outer2");
		checkTransaction();
		
		TestVo testVo = createTestVo("test1.outer2");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		mapper1.insert(testVo);
		if (innerException) {
			// inner: rollback, outer: commit
			try {
				log.info("\t > calling innerRequiresNew1...");
				service2.innerRequiresNew1();
			} catch (Exception e) {
				log.info("\t > catch {} at innerRequiresNew1", e.getClass().getSimpleName());
			}
		} else {
			log.info("\t > calling innerRequiresNew2...");
			service2.innerRequiresNew2();
		}

		// inner: rollback, outer: rollback
//		if (innerException) {
//			log.info("\t > calling innerRequiresNew1...");
//			service2.innerRequiresNew1();
//		} else {
//			log.info("\t > calling innerRequiresNew2...");
//			service2.innerRequiresNew2();
//		}
	}
	
	@Transactional
	public void outer3() {
		log.info("## outer3");
		checkTransaction();
		
		TestVo testVo = createTestVo("test1.outer3");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		mapper1.insert(testVo);
		
		log.info("\t > calling innerRequiresNew2...");
		service2.innerRequiresNew2();
		
		log.info("\t > will throw RuntimeException in outer3");
		throw new RuntimeException("outer3");
	}
	
	public void outer4() {
		log.info("## outer4");
		checkTransaction();
		
		TestVo testVo = createTestVo("test1.outer4");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		mapper1.insert(testVo);
		
		log.info("\t > calling innerMandatory...");
		service2.innerMandatory();
	}
	
	@Transactional
	public void outer5() {
		log.info("## outer5");
		checkTransaction();
		
		TestVo testVo = createTestVo("test1.outer5");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		mapper1.insert(testVo);
		try {
			log.info("\t > calling innerNested1...");
			service2.innerNested1();
		} catch (Exception e) {
			log.info("\t > catch {} at innerNested1", e.getClass().getSimpleName());
		}
	}

	@Transactional
	public void outer6() {
		log.info("## outer6");
		checkTransaction();
		
		TestVo testVo = createTestVo("test1.outer6");
		applicationEventPublisher.publishEvent(new TestEvent(
				TransactionSynchronizationManager.getCurrentTransactionName(), testVo));
		
		log.info("\t > insert testVo ( name = {} )", testVo.getName());
		mapper1.insert(testVo);
		
		log.info("\t > calling innerNested2...");
		service2.innerNested2();
	}

	@Transactional(isolation = Isolation.DEFAULT)
	public void isoDefault() {
		log.info("## Isolation.DEFAULT");
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void isoReadCommitted() {
		log.info("## Isolation.READ_COMMITTED");
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void isoReadUncommitted() {
		log.info("## Isolation.READ_UNCOMMITTED");
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void isoRepeatableRead() {
		log.info("## Isolation.REPEATABLE_READ");
	}
	
}
