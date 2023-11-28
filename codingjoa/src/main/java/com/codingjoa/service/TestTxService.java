package com.codingjoa.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
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
	
	public List<TestVo> selectAll() {
		log.info("## TestTxService.selectAll");
		return testMapper.selectAll();
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

	/*******************************************************************/
	// invoke, payment 
	/*******************************************************************/
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;
	
	@Transactional
	public String invoke() {
		log.info("*** invoke start");
		insert1();
		insert2();
		log.info("*** invoke end");
		return "tx invoked";
	}
	
	public void payment() {
		log.info("*** payment start");
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		log.info("\t > tx definition = {}", txDefinition);
		
		TransactionStatus status = mainTransactionManager.getTransaction(txDefinition);
		checkTransaction(status);
		
		try {
			//testMapper.account(account);          // 결제금액 저장
			//testMapper.paymentType(paymentType);  // 결제정보 저장(ex. 카드, 계좌이체 정보 등)
            mainTransactionManager.commit(status);
        } catch(RuntimeException e) {
            mainTransactionManager.rollback(status);
            throw e;
        } finally {
        	checkTransaction(status);
        	log.info("*** payment end");
        }
	}
	
	private void checkTransaction(TransactionStatus status) {
		log.info("## checkTransaction");
		log.info("\t > tx name = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("\t > tx status = {}", status);
		if (status == null) {
			log.info("\t > NO TX");
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
	
	private void insert1() {
		log.info("## insert1");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(6))
				.name("a1")
				.password("a1")
				.regdate(LocalDateTime.now())
				.build();
		testMapper.insert(testVo);
	}
	
	private void insert2() {
		log.info("## insert2");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(6))
				.name("a2")
				.password("a2")
				.regdate(LocalDateTime.now())
				.build();
		testMapper.insert(testVo);
	}
	
}
