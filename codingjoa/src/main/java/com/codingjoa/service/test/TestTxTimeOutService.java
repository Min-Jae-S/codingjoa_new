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
//	@Transactional
	public void induceDelayByDB() {
		log.info("## induceDelayByDB");
		applicationEventPublisher.publishEvent("induceDelayByDB");

		txService.insertRandomNumber();
		timeoutMapper.delay1(10);
//		try {
//			timeoutMapper.delay1(10);
//		} catch (Exception e) {
//			if (e instanceof RuntimeException) {
//				log.info("\t > {} - unchecked exception", e.getClass().getSimpleName());
//			} else {
//				log.info("\t > {} - checked exception", e.getClass().getSimpleName());
//			}
//		}
	}
	
	// internal delay by thread
	@Transactional(timeout = 5)
	public void induceDelayByThread() {
		log.info("## induceDelayByThread");
		applicationEventPublisher.publishEvent(true);
		try {
			txService.insertRandomNumber();
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
			Thread.currentThread().interrupt();
		}
	}
}
