package com.codingjoa.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.mapper.test.TestTimeoutMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestTxTimeOutService {
	
	@Autowired
	private TestTimeoutMapper timeoutMapper;
	
	@Autowired
	private TestTxService txServivce;

	@Transactional(timeout = 5)
	public void invokeDelay() {
		log.info("## invokeDelay");
		txServivce.insertRandomNumber();
		txServivce.insertRandomNumber();
		timeoutMapper.sleep(10);
//		try {
//			timeoutMapper.sleep(10);
//		} catch (Exception e) {
//			String type = "";
//			if (e instanceof RuntimeException) {
//				type = "unchecked exception";
//			} else {
//				type = "checked exception";
//			}
//			log.info("\t > {} - {}", e.getClass().getSimpleName(), type);
//		}
		
//		try {
//			TimeUnit.SECONDS.sleep(10);
//			//Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			log.info("\t > {}", e.getClass().getSimpleName());
//			Thread.currentThread().interrupt();
//		}
	}
	
}
