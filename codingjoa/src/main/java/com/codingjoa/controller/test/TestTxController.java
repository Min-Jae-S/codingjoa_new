package com.codingjoa.controller.test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestTxService;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestTxController {
	
	/*
	 * @ ApplicationContext.getBeansOfType -> BeanFactoryUtils.beansOfTypeIncludingAncestors
	 * 
	 * https://github.com/spring-projects/spring-framework/issues/15553
	 * Calling ApplicationContext.getBeansOfType(Class) intentionally does not consider the parent hierarchy (see the java doc). 
	 * You can use the BeanFactoryUtils class if you want to search the full hierarchy.
	 */
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private TestTxService service;
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;

	@GetMapping("/tx/datasources")
	public ResponseEntity<Object> datasources() {
		log.info("## datasources");
		Map<String, DataSource> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DataSource.class);
		if (!map.isEmpty()) {
			for (String dataSource : map.keySet()) {
				log.info("\t > {}", dataSource);
			}
		} else {
			log.info("\t > no DataSource");
		}
		
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx/managers")
	public ResponseEntity<Object> managers() {
		log.info("## managers");
		Map<String, PlatformTransactionManager> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PlatformTransactionManager.class);
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				//log.info("\t > {} = {}", key, map.get(key));
				log.info("\t > {}", key);
			}
		} else {
			log.info("\t > no PlatformTransactionManager");
		}
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx/factory")
	public ResponseEntity<Object> factory() {
		log.info("## factory");
		Map<String, SqlSessionFactory> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, SqlSessionFactory.class);
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				log.info("\t > {} = {}", key, map.get(key));
			}
		} else {
			log.info("\t > no SqlSessionFactory");
		}
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/template")
	public ResponseEntity<Object> template() {
		log.info("## template");
		Map<String, SqlSessionTemplate> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, SqlSessionTemplate.class);
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				log.info("\t > {} = {}", key, map.get(key));
			}
		} else {
			log.info("\t > no SqlSessionTemplate");
		}
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/sync-manager")
	public ResponseEntity<Object> syncManager() {
		log.info("## syncManager");
		Map<String, TransactionSynchronizationManager> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, TransactionSynchronizationManager.class);
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				log.info("\t > {} = {}", key, map.get(key));
			}
		} else {
			log.info("\t > no TransactionSynchronizationManager");
		}
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/test1")
	public ResponseEntity<Object> test1() { 
		log.info("## test1");
		service.doSomething1(); // non-tx calling tx 
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		service.doSomething2(); // tx calling tx
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		service.doSomething3(); // calling tx
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx/test4")
	public ResponseEntity<Object> test4() {
		log.info("## test4");
		service.doSomething4(); // calling tx + transaction-sync
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/select-all")
	public ResponseEntity<Object> selectAll() {
		log.info("## selectAll");
		List<TestVo> result = service.selectAll();
		if (result.size() > 0) {
			result.forEach(testVo -> log.info("\t > {}", testVo));
		} else {
			log.info("\t > no records");
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping("/tx/select-all2")
	public ResponseEntity<Object> selectAll2() {
		log.info("## selectAll2");
		List<TestVo> result = service.selectAll2();
		if (result.size() > 0) {
			result.forEach(testVo -> log.info("\t > {}", testVo));
		} else {
			log.info("\t > no records");
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping("/tx/insert-no-tx")
	public ResponseEntity<Object> insertNoTx() {
		log.info("## insertNoTx");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name("withoutTx")
				.password("withoutTx")
				.regdate(LocalDateTime.now())
				.build();
		log.info("\t > created testVo = {}", testVo);
		
		int result = service.insertNoTx(testVo);
		log.info("\t > inserted rows = {}", result);
		
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/insert-tx")
	public ResponseEntity<Object> insertTx() {
		log.info("## insertTx");
		TestVo testVo = TestVo.builder()
				.id(RandomStringUtils.randomAlphanumeric(8))
				.name("insertTx")
				.password("insertTx")
				.regdate(LocalDateTime.now())
				.build();
		log.info("\t > created testVo = {}", testVo);
		
		int result = service.insertTx(testVo);
		log.info("\t > inserted rows = {}", result);
		
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/update")
	public ResponseEntity<Object> update() {
		log.info("## update");
		TestVo testVo = TestVo.builder()
				.name("modified")
				.password("modified")
				.build();
		log.info("\t > created testVo = {}", testVo);
		
		int result = service.update(testVo);
		log.info("\t > updated rows = {}", result);
		
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/remove")
	public ResponseEntity<Object> remove() {
		log.info("## remove");
		int result = service.remove();
		log.info("\t > removed rows = {}", result);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx/remove-all")
	public ResponseEntity<Object> removeAll() {
		log.info("## removeAll");
		int result = service.removeAll();
		log.info("\t > removed rows = {}", result);
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/remove-all2")
	public ResponseEntity<Object> removeAll2() {
		log.info("## removeAll2");
		int result = service.removeAll2();
		log.info("\t > removed rows = {}", result);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx/invoke")
	public ResponseEntity<Object> invoke() throws Exception {
		log.info("## invoke");
		service.invoke();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/invoke-no-tx")
	public ResponseEntity<Object> invokeNoTx() {
		log.info("## invokeNoTx");
		service.invokeNoTx(); 
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/tx/invoke-tx")
	public ResponseEntity<Object> invokeTx() {
		log.info("## invokeTx");
		service.invokeTx(); 
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/payment")
	public ResponseEntity<Object> payment() {
		log.info("## payment");
		service.payment(); 
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/invoke/sqlSession")
	public ResponseEntity<Object> invokeSqlSession() throws Exception {
		log.info("## invokeSqlSession");
		service.invokeSqlSession();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/invoke/sqlSessionTemplate")
	public ResponseEntity<Object> invokeSqlSessionTemplate() throws Exception {
		log.info("## invokeSqlSessionTemplate");
		service.invokeSqlSessionTemplate();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/tx/invoke/mapper")
	public ResponseEntity<Object> invokeMapper() throws Exception {
		log.info("## invokeMapper");
		service.invokeMapper();
		return ResponseEntity.ok("success");
	}
	
}
