package com.codingjoa.service.test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.test.TestInnerMapper;
import com.codingjoa.mapper.test.TestIsoMapper;
import com.codingjoa.mapper.test.TestOuterMapper;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

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
	 * @@ https://escapefromcoding.tistory.com/810?category=1246236
	 * @@ https://velog.io/@betterfuture4/Spring-Transactional-%EC%B4%9D%EC%A0%95%EB%A6%AC
	 * @@ https://meaownworld.tistory.com/entry/JDBC-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98%EA%B3%BC-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%B6%94%EC%83%81%ED%99%94-Transaction-Abstraction-of-Transaction
	 * 		- Transaction abstaction
	 * 			> PlatformTransactionManager (DataSourceTransactionManager, JpaTransactionManager, HibernateTransactionManager, etc...)
	 * 		- Transaction synchronization
	 * 		- Declarative transaction management
	 */
	
	// @@ https://cheese10yun.github.io/spring-transacion-same-bean/
	// 즉, 위와 같은 경우에는 동일한 Bean(Class)에서 'Spring AOP CGLIB'이 동작하지 않습니다.
	
	// @@ https://obv-cloud.com/39
	// Transaction은 Connection 단위로 이루어지기 때문에 하나의 요청에 같은 Connection을 사용할 수 있도록 보장해야 한다.
	// 이 때, 사용되는 것이 TransactionSynchronizationManager이다.
	// 		1. Transaction 시작
	// 		2. Datasource에서 Connection 획득
	// 		3. Connection을 TransactionSynchronizationManager의 ThreadLocal에 저장
	// 		4. Repository에서 TransactionSynchronizationManager을 통해 Connection 획득 후 처리
	// 		5. 비즈니스 로직 수행 완료
	// 		6. Transaction commit or rollback
	// 		7. TransactionSynchronizationManager의 ThreadLocal에서 Connection 제거
	// 		8. Datasource에 Connection 반환
	
	@Autowired
	private TestOuterMapper outerMapper;

	@Autowired
	private TestInnerMapper innerMapper;
	
	@Autowired
	private TestIsoMapper isoMapper;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	public void doSomething1() {
		log.info("## doSomething1 - NO Transactional");
		log.info("\t > calling doSomething3");
		doSomething3();
	}
	
	@Transactional
	public void doSomething2() {
		log.info("## doSomething2 - Transactional");
		log.info("\t > calling doSomething3");
		doSomething3();
	}
	
	@Transactional
	public void doSomething3() {
		log.info("## doSomething3 - Transactional");
		TransactionStatus status = null;
		try {
			status = TransactionAspectSupport.currentTransactionStatus();
		} catch (NoTransactionException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			checkTransaction(status);
			checkTransactionBySyncManager();
		}
	}

	@Transactional
	public void doSomething4() {
		log.info("## doSomething4 - Transactional");
		TransactionSynchronizationManager.registerSynchronization(
			new TransactionSynchronizationAdapter() {
				@Override
				public void beforeCommit(boolean readOnly) {
					log.info("*** beforeCommit");
				}

				@Override
				public void afterCommit() {
					log.info("*** afterCommit");
				}
			});
		
		TransactionStatus transactionStatus = null;
		try {
			transactionStatus = TransactionAspectSupport.currentTransactionStatus();
		} catch (NoTransactionException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		} finally {
			checkTransaction(transactionStatus);
			checkTransactionBySyncManager();
		}
	}
	
	public List<TestVo> selectAll() {
		return outerMapper.selectAll();
	}

	public List<TestVo> selectAll2() {
		return innerMapper.selectAll();
	}
	
	public int insertNoTx(TestVo testVo) {
		return outerMapper.insert(testVo);
	}

	@Transactional
	public int insertTx(TestVo testVo) {
		return outerMapper.insert(testVo);
	}
	
	public int update(TestVo testVo) {
		return outerMapper.update(testVo);
	}
	
	public int remove() {
		return outerMapper.remove();
	}
	
	public int removeAll() {
		return outerMapper.removeAll();
	}

	public int removeAll2() {
		return innerMapper.removeAll();
	}
	
	private void checkTransaction(TransactionStatus transactionStatus) {
		if (transactionStatus == null) {
			log.info("\t > NO transaction");
			return;
		}
		
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
	}
	
	private void checkTransactionBySyncManager() {
		log.info("## checkTransactionBySyncManager");
		log.info("\t > current tx = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("\t > tx active = {}", TransactionSynchronizationManager.isActualTransactionActive());
		
		boolean syncActive = TransactionSynchronizationManager.isSynchronizationActive();
		log.info("\t > sync active = {}", syncActive);
		log.info("\t > syncs = {}", syncActive == true ? TransactionSynchronizationManager.getSynchronizations() : "no sync");
	}

	/********************************************************/
	/* invoke(), invokeNoTx(), invokeTx(), payment()		*/ 
	/********************************************************/
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void invokeSqlSession() throws SQLException {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		log.info("\t > auto commit = {}", sqlSession.getConnection().getAutoCommit());
		int result = sqlSession.getMapper(TestOuterMapper.class).insert(createTestVo());
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
		int result = outerMapper.insert(createTestVo());
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
		int result = outerMapper.insert(createTestVo());
		log.info("\t > inserted rows = {}", result);
		log.info("*** invokeNoTx end");
	}

	@Transactional
	public void invokeTx() {
		log.info("*** invokeTx start");
		int result = outerMapper.insert(createTestVo());
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
		outerMapper.insert(testVo);
	}
	
	private void insertA2() {
		log.info("## insertA2");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name("A2")
				.password("A2")
				.regdate(LocalDateTime.now())
				.build();
		outerMapper.insert(testVo);
	}
	
	// @@ ISOLATION LEVEL
	public List<Integer> findNumbers() {
		return isoMapper.findNumbers();
	}

	public Integer findCurrentNumber() {
		return isoMapper.findCurrentNumber();
	}
	
	@Transactional
	public void insertRandomNumber() {
		applicationEventPublisher.publishEvent("insertRandomNumber");
		int randomNumber = RandomUtils.nextInt(1, 999);
		int result = isoMapper.insertNumber(randomNumber);
		if (result > 0) {
			log.info("\t > insert random number {}", randomNumber);
		} else {
			log.info("\t > insert fail");
		}
	}
	
	@Transactional
	public void updateCurrentNumber() {
		applicationEventPublisher.publishEvent("updateCurrentNumber");
		int num = 0;
		int result = isoMapper.updateCurrentNumber(num);
		if (result > 0) {
			log.info("\t > update current number to {}", num);
		} else {
			log.info("\t > update fail");
		}
	}
	
	@Transactional
	public void deleteNumbers() {
		applicationEventPublisher.publishEvent("deleteNumbers");
		isoMapper.deleteNumbers();
		log.info("\t > delete all numbers");
	}

	@Transactional
	public void deleteCurrentNumber() {
		applicationEventPublisher.publishEvent("deleteCurrentNumber");
		int result = isoMapper.deleteCurrentNumber();
		if (result > 0) {
			log.info("\t > delete current number");
		} else {
			log.info("\t > there is no record OR delete fail");
		}
	}
}
