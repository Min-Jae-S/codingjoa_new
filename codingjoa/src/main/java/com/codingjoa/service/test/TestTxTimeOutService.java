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
	
	@SuppressWarnings("unused")
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	// external delay by database
	@Transactional (timeout = 5) 
	public void induceDelayByDB() {
		log.info("## induceDelayByDB");

		txService.insertRandomNumber();
		timeoutMapper.delay1(10);
		
//		try {
//			timeoutMapper.delay1(10);
//		} catch (Exception e) {
//			String type = "";
//			if (e instanceof RuntimeException) {
//				type = "unchecked exception";
//			} else {
//				type = "checked exception";
//			}
//			log.info("\t > {} - {}", e.getClass().getSimpleName(), type);
//		}
	}
	
	// internal delay by thread
	@Transactional(timeout = 5)
	public void induceDelayByThread() {
		log.info("## induceDelayByThread");
		try {
			txService.insertRandomNumber();
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
			Thread.currentThread().interrupt();
		}
	}
}
