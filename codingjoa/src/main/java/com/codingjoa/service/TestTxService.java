package com.codingjoa.service;

import static org.mybatis.spring.SqlSessionUtils.isSqlSessionTransactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
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

@SuppressWarnings("unused")
@Slf4j
@Service
public class TestTxService {
	
	/*
	 * @@ Transaction properties
	 * 	DefaultTransactionDefinition implements TransactionDefinition { ... }
	 * 		- Transaction Propagation
	 * 		- Isolation Level
	 * 		- Timeout
	 * 		- Read Only
	 * 
	 * @@ Programmatic transaction management
	 * @@ Declarative transaction management
	 * 
	 */
	
	@Autowired
	private TestMapper testMapper;
	
	public void doSomething1() {
		log.info("## doSomething1 (NO @Transactional)");
		log.info("\t > calling doSomething3");
		doSomething3();
	}
	
	@Transactional
	public void doSomething2() {
		log.info("## doSomething2 (@Transactional)");
		log.info("\t > calling doSomething3");
		doSomething3();
	}

	@Transactional
	public void doSomething3() {
		log.info("## doSomething3 (@Transactional)");
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
		log.info("## doSomething4 (@Transactional)");
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
	
	public int insertNoTx(TestVo testVo) {
		log.info("## TestTxService.insertNoTx");
		return testMapper.insert(testVo);
	}

	@Transactional
	public int insertTx(TestVo testVo) {
		log.info("## TestTxService.insertTx");
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
	
	public int removeAll() {
		log.info("## TestTxService.removeAll");
		return testMapper.removeAll();
	}
	
	private void checkTransaction(TransactionStatus status) {
		log.info("## checkTransaction");
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

	/*******************************************************************/
	// invoke(), invokeNoTx(), invokeTx(), payment() 
	/*******************************************************************/
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void invokeSqlSession() throws SQLException {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		log.info("\t > auto commit = {}", sqlSession.getConnection().getAutoCommit());
		int result = sqlSession.getMapper(TestMapper.class).insert(createTestVo());
		log.info("\t > inserted rows = {}", result);
		sqlSession.close();
	}
	
	/*
	 * @@ org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor
	 * 
	 * 	private class SqlSessionInterceptor implements InvocationHandler {
	 * 		@Override 
	 * 		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	 * 			...
	 * 			if (!isSqlSessionTransactional(sqlSession, SqlSessionTemplate.this.sqlSessionFactory)) {
	 * 				// force commit even on non-dirty sessions because some databases require 
	 * 				// a commit/rollback before calling close()
	 * 				sqlSession.commit(true);
	 * 			}
	 * 		}
	 */
	
	public void invokeSqlSessionTemplate() {
		int result = sqlSessionTemplate.insert("com.codingjoa.mapper.TestMapper.insert", createTestVo());
		log.info("\t > inserted rows = {}", result);
	}
	
	public void invokeMapper() {
		int result = testMapper.insert(createTestVo());
		log.info("\t > inserted rows = {}", result);
	}
	
	@Transactional
	public void invoke() throws Exception {
		log.info("*** invoke start");
		insertA1();
		insertA2();
		log.info("*** invoke end");
	}

	public void invokeNoTx() {
		log.info("*** invokeNoTx start");
		int result = testMapper.insert(createTestVo());
		log.info("\t > inserted rows = {}", result);
		log.info("*** invokeNoTx end");
	}

	@Transactional
	public void invokeTx() {
		log.info("*** invokeTx start");
		int result = testMapper.insert(createTestVo());
		log.info("\t > inserted rows = {}", result);
		log.info("*** invokeTx end");
	}
	
	private TestVo createTestVo() {
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name("test")
				.password("test")
				.regdate(LocalDateTime.now())
				.build();
		log.info("\t > created testVo = {}", testVo);
		return testVo;
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
	
	private void insertA1() {
		log.info("## insertA1");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name("A1")
				.password("A1")
				.regdate(LocalDateTime.now())
				.build();
		testMapper.insert(testVo);
	}
	
	private void insertA2() {
		log.info("## insertA2");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name("A2")
				.password("A2")
				.regdate(LocalDateTime.now())
				.build();
		testMapper.insert(testVo);
	}
	
}
