package com.codingjoa.service.test;

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
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.TestMapper;
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
	// 이 때 사용되는 것이 TransactionSynchronizationManager이다.
	// 		1. Transaction 시작
	// 		2. Datasource에서 Connection 획득
	// 		3. Connection을 TransactionSynchronizationManager의 ThreadLocal에 저장
	// 		4. Repository에서 TransactionSynchronizationManager을 통해 Connection 획득 후 처리
	// 		5. 비즈니스 로직 수행 완료
	// 		6. Transaction commit or rollback
	// 		7. TransactionSynchronizationManager의 ThreadLocal에서 Connection 제거
	// 		8. Datasource에 Connection 반환
	
	@Autowired
	private TestMapper mapper;
	
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
			checkTransactionBySyncManager();
		}
	}

	@Transactional
	public void doSomething4() {
		log.info("## doSomething4 (@Transactional)");
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
	
	public List<TestVo> selectAll() {
		log.info("## TestTxService.selectAll");
		return mapper.selectAll();
	}
	
	public int insertNoTx(TestVo testVo) {
		log.info("## TestTxService.insertNoTx");
		return mapper.insert(testVo);
	}

	@Transactional
	public int insertTx(TestVo testVo) {
		log.info("## TestTxService.insertTx");
		return mapper.insert(testVo);
	}
	
	public int update(TestVo testVo) {
		log.info("## TestTxService.update");
		return mapper.update(testVo);
	}
	
	public int remove() {
		log.info("## TestTxService.remove");
		return mapper.remove();
	}
	
	public int removeAll() {
		log.info("## TestTxService.removeAll");
		return mapper.removeAll();
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
	
	private void checkTransactionBySyncManager() {
		log.info("## checkTransactionBySyncManager");
		log.info("\t > current tx = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("\t > tx active = {}", TransactionSynchronizationManager.isActualTransactionActive());
		
		boolean syncActive = TransactionSynchronizationManager.isSynchronizationActive();
		log.info("\t > sync active = {}", syncActive);
		log.info("\t > syncs = {}", syncActive == true ? TransactionSynchronizationManager.getSynchronizations() : "no sync");
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
		int result = mapper.insert(createTestVo());
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
		int result = mapper.insert(createTestVo());
		log.info("\t > inserted rows = {}", result);
		log.info("*** invokeNoTx end");
	}

	@Transactional
	public void invokeTx() {
		log.info("*** invokeTx start");
		int result = mapper.insert(createTestVo());
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
		mapper.insert(testVo);
	}
	
	private void insertA2() {
		log.info("## insertA2");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name("A2")
				.password("A2")
				.regdate(LocalDateTime.now())
				.build();
		mapper.insert(testVo);
	}
	
}