package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.service.test.TestJdbcTxService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestJdbcTxController {

	@Autowired
	private TestJdbcTxService testJdbcTxService;
	
	@GetMapping("/jdbc-tx/tx/{commit}")
	public ResponseEntity<Object> useTx(@PathVariable Boolean commit) { 
		log.info("## useTx");
		log.info("\t > {}", (commit) ? "commit" : "rollback");
		testJdbcTxService.useTx(commit);
		return ResponseEntity.ok("success");
	} 
	
	@GetMapping("/jdbc-tx/sync-manager/{commit}") 
	public ResponseEntity<Object> useTxSyncManager(@PathVariable Boolean commit) { 
		log.info("## useTxSyncManager");
		log.info("\t > {}", (commit) ? "commit" : "rollback");
		testJdbcTxService.useTxSyncManager(commit);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/jdbc-tx/manager/{commit}") 
	public ResponseEntity<Object> useTxManager(@PathVariable Boolean commit) { 
		log.info("## useTxManager");
		log.info("\t > {}", (commit) ? "commit" : "rollback");
		testJdbcTxService.useTxManager(commit);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/jdbc-tx/declarative-tx/{commit}") 
	public ResponseEntity<Object> usetDeclarativeTx(@PathVariable Boolean commit) { 
		log.info("## usetDeclarativeTx");
		log.info("\t > {}", (commit) ? "commit" : "rollback");
		testJdbcTxService.usetDeclarativeTx(commit);
		return ResponseEntity.ok("success");
	}
	
}
