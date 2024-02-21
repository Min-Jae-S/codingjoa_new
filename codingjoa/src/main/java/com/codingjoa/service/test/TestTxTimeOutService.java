package com.codingjoa.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.mapper.test.TestIsoMapper;
import com.codingjoa.mapper.test.TestTimeoutMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxTimeOutService {
	
	@SuppressWarnings("unused")
	@Autowired
	private TestTimeoutMapper timeoutMapper;
	
	@Autowired
	private TestTxService txService;

	@SuppressWarnings("unused")
	@Autowired
	private TestIsoMapper isoMapper;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	// external delay by database
	@Transactional (timeout = 5) 
	public void induceDelayByDB() {
		log.info("## induceDelayByDB");
		applicationEventPublisher.publishEvent("induceDelayByDB");
		txService.deleteCurrentNumber();
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
