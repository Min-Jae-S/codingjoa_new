package com.codingjoa.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.mapper.test.TestIsoMapper;
import com.codingjoa.mapper.test.TestTimeoutMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Service
public class TestTxTimeOutService {
	
	@Autowired
	private TestTimeoutMapper timeoutMapper;
	
	@Autowired
	private TestTxService txService;

	@Autowired
	private TestIsoMapper isoMapper;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	// external delay by database
	@Transactional (timeout = 1) 
	public void induceDelayByDB() {
		log.info("## induceDelayByDB");
		applicationEventPublisher.publishEvent("induceDelayByDB");
		for (int i = 0; i < 10000; i++) {
			txService.insertRandomNumber();
		}
	}
	
	// internal delay by thread
	@Transactional(timeout = 5)
	public void induceDelayByThread() {
		log.info("## induceDelayByThread");
		applicationEventPublisher.publishEvent("induceDelayByThread");
		try {
			txService.deleteCurrentNumber();
			Thread.sleep(10000); // 10sec
		} catch (InterruptedException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
			Thread.currentThread().interrupt();
		}
	}
}
