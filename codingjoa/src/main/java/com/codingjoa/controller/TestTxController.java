package com.codingjoa.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.service.TestTxService;
import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
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
	private TestTxService testTxService;
	
	@Resource(name = "mainTransactionManager")
	private PlatformTransactionManager mainTransactionManager;

	@Resource(name = "subTransactionManager")
	private PlatformTransactionManager subTransactionManager;
	
	@Autowired(required = false)
	private TransactionSynchronizationManager transactionSyncManager;
	
	@GetMapping("/tx")
	public String main() {
		log.info("## main");
		return "test/tx";
	}
	
	@ResponseBody
	@GetMapping("/tx/datasources")
	public ResponseEntity<Object> datasources() {
		log.info("## datasources");
		Map<String, DataSource> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DataSource.class);
		if (map.isEmpty()) {
			log.info("\t > no DataSource");
		} else {
			for (String dataSource : map.keySet()) {
				log.info("\t > {}", dataSource);
			}
		}
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx/managers")
	public ResponseEntity<Object> managers() {
		log.info("## managers");
		Map<String, PlatformTransactionManager> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PlatformTransactionManager.class);
		if (map.isEmpty()) {
			log.info("\t > no PlatformTransactionManager");
		} else {
			for (String key : map.keySet()) {
				//log.info("\t > {} = {}", key, map.get(key));
				log.info("\t > {}", key);
			}
		}
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx/factory")
	public ResponseEntity<Object> factory() {
		log.info("## factory");
		Map<String, SqlSessionFactory> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, SqlSessionFactory.class);
		if (map.isEmpty()) {
			log.info("\t > no SqlSessionFactory");
		} else {
			for (String key : map.keySet()) {
				log.info("\t > {} = {}", key, map.get(key));
			}
		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/template")
	public ResponseEntity<Object> template() {
		log.info("## template");
		Map<String, SqlSessionTemplate> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, SqlSessionTemplate.class);
		if (map.isEmpty()) {
			log.info("\t > no SqlSessionTemplate");
		} else {
			for (String key : map.keySet()) {
				log.info("\t > {} = {}", key, map.get(key));
			}
		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/sync-manager")
	public ResponseEntity<Object> syncManager() {
		log.info("## syncManager");
		log.info("\t > autowired TransactionSynchronizationManager = {}", transactionSyncManager);
		Map<String, TransactionSynchronization> map1 = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, TransactionSynchronization.class);
		if (map1.isEmpty()) {
			log.info("\t > no TransactionSynchronization");
		} else {
			for (String key : map1.keySet()) {
				log.info("\t > {} = {}", key, map1.get(key));
			}
		}
		
		Map<String, TransactionSynchronizationManager> map2 = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, TransactionSynchronizationManager.class);
		if (map2.isEmpty()) {
			log.info("\t > no TransactionSynchronizationManager");
		} else {
			log.info("==========================================================================================");
			for (String key : map2.keySet()) {
				log.info("\t > {} = {}", key, map2.get(key));
			}
		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test1")
	public ResponseEntity<Object> test1() { 
		log.info("## test1");
		testTxService.doSomething1(); // non-tx calling tx 
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		testTxService.doSomething2(); // tx calling tx
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		testTxService.doSomething3(); // calling tx
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx/test4")
	public ResponseEntity<Object> test4() {
		log.info("## test4");
		testTxService.doSomething3(); // calling tx (mainTxManager)
		testTxService.doSomething4(); // calling tx (subTxManager)
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/select-all")
	public ResponseEntity<Object> selectAll() {
		log.info("## selectAll");
		List<TestVo> result = testTxService.selectAll();
		if (result.size() > 0) {
			result.forEach(testVo -> log.info("\t > {}", testVo));
		} else {
			log.info("\t > no records");
		}
		return ResponseEntity.ok(result);
	}

	@ResponseBody
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
		
		int result = testTxService.insertNoTx(testVo);
		log.info("\t > inserted rows = {}", result);
		
		return ResponseEntity.ok("success");
	}

	@ResponseBody
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
		
		int result = testTxService.insertTx(testVo);
		log.info("\t > inserted rows = {}", result);
		
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/update")
	public ResponseEntity<Object> update() {
		log.info("## update");
		TestVo testVo = TestVo.builder()
				.name("modified")
				.password("modified")
				.build();
		log.info("\t > created testVo = {}", testVo);
		
		int result = testTxService.update(testVo);
		log.info("\t > updated rows = {}", result);
		
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/remove")
	public ResponseEntity<Object> remove() {
		log.info("## remove");
		int result = testTxService.remove();
		log.info("\t > removed rows = {}", result);
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx/remove-all")
	public ResponseEntity<Object> removeAll() {
		log.info("## removeAll");
		int result = testTxService.removeAll();
		log.info("\t > removed rows = {}", result);
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx/invoke")
	public ResponseEntity<Object> invoke() throws Exception {
		log.info("## invoke");
		testTxService.invoke();
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/invoke-no-tx")
	public ResponseEntity<Object> invokeNoTx() {
		log.info("## invokeNoTx");
		testTxService.invokeNoTx(); 
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/tx/invoke-tx")
	public ResponseEntity<Object> invokeTx() {
		log.info("## invokeTx");
		testTxService.invokeTx(); 
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/payment")
	public ResponseEntity<Object> payment() {
		log.info("## payment");
		testTxService.payment(); 
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/invoke/sqlSession")
	public ResponseEntity<Object> invokeSqlSession() throws Exception {
		log.info("## invokeSqlSession");
		testTxService.invokeSqlSession();
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/invoke/sqlSessionTemplate")
	public ResponseEntity<Object> invokeSqlSessionTemplate() throws Exception {
		log.info("## invokeSqlSessionTemplate");
		testTxService.invokeSqlSessionTemplate();
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/invoke/mapper")
	public ResponseEntity<Object> invokeMapper() throws Exception {
		log.info("## invokeMapper");
		testTxService.invokeMapper();
		return ResponseEntity.ok("success");
	}
	
}
