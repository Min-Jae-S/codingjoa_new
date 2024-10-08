package com.codingjoa.service.test;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.codingjoa.test.TestEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TxEventService {
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void commitEvent(Object obj) {
		log.info("------------- commit -------------");
		if (obj instanceof TestEvent) {
			TestEvent event = (TestEvent) obj;
			log.info("\t > location = {}", event.getLocation());
			log.info("\t > commit testVo ( name = {} )", event.getTestVo().getName());
		} else {
			log.info("\t > commit by '{}'", obj);
		}
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
	public void rollbackEvent(Object obj) {
		log.info("------------- rollback -------------");
		if (obj instanceof TestEvent) {
			TestEvent event = (TestEvent) obj;
			log.info("\t > location = {}", event.getLocation());
			log.info("\t > rollback testVo ( name = {} )", event.getTestVo().getName());
		} else {
			log.info("\t > rollback by '{}'", obj);
		}
	}

//	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
//	public void completionEvent(Object obj) {
//		log.info("------------- completion -------------");
//		if (obj instanceof TestEvent) {
//			TestEvent event = (TestEvent) obj;
//			log.info("\t > location = {}", event.getLocation());
//			log.info("\t > complete testVo ( name = {} )", event.getTestVo().getName());
//		} else {
//			log.info("\t > complete by '{}'", obj);
//		}
//	}
	
}
