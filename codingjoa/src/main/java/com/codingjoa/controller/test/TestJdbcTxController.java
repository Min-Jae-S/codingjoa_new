package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@GetMapping("/jdbc-tx/sync-manager/{commit}") 
	public ResponseEntity<Object> useTxSyncManager(@PathVariable Boolean commit) { 
		log.info("## useTxSyncManager");
		return ResponseEntity.ok("success");
	}
	
	@DeleteMapping("/jdbc-tx/manager/{commit}") 
	public ResponseEntity<Object> useTxManager(@PathVariable Boolean commit) { 
		log.info("## useTxManager");
		return ResponseEntity.ok("success");
	}
}
