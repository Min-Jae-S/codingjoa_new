package com.codingjoa.controller;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/tx")
	public String main() {
		log.info("## tx main");
		return "test/tx";
	}
	
	@ResponseBody
	@GetMapping("/tx/txManagers")
	public ResponseEntity<Object> txManagers() {
		log.info("## txManagers");
		Map<String, PlatformTransactionManager> map = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PlatformTransactionManager.class);
		for(String key : map.keySet()) {
//			log.info("\t > {} = {}", key, map.get(key));
			log.info("\t > {}", key);
		}
//		log.info("\t > autrowiredTransactionManager = {}", mainTransactionManager);
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/dataSources")
	public ResponseEntity<Object> dataSources() {
		log.info("## dataSources");
		Set<String> dataSources = 
				BeanFactoryUtils.beansOfTypeIncludingAncestors(context, DataSource.class).keySet(); 
		for(String dataSource : dataSources) {
			log.info("\t > {}", dataSource);
		}
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		testTxService.doSomething1(); // doSomething1(NO @Transactional) -> doSomething3(@Transactional)
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		testTxService.doSomething2(); // doSomething2(@Transactional) -> doSomething3(@Transactional)
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		testTxService.doSomething3(); // doSomething3(@Transactional)
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/tx/select")
	public ResponseEntity<Object> select() {
		log.info("## select");
		return ResponseEntity.ok("select success");
	}

	@ResponseBody
	@GetMapping("/tx/insert")
	public ResponseEntity<Object> insert() {
		log.info("## insert");
		TestVo testVo = TestVo.builder()
				.id("smj20228")
				.name("minjae")
				.password(passwordEncoder.encode(""))
				.build();
		log.info("\t > testVo = {}", testVo);
		return ResponseEntity.ok("insert success");
	}

	@ResponseBody
	@GetMapping("/tx/update")
	public ResponseEntity<Object> update() {
		log.info("## update");
		return ResponseEntity.ok("update success");
	}

	@ResponseBody
	@GetMapping("/tx/remove")
	public ResponseEntity<Object> remove() {
		log.info("## remove");
		return ResponseEntity.ok("remove success");
	}
	
}
