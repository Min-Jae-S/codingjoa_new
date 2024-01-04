package com.codingjoa.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.codingjoa.test.TestEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionEventService {
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void afterCommit(TestEvent event) {
		log.info("## afterCommit");
		log.info("\t > location = {}", event.getLocation());
		log.info("\t > commit testVo ( name = {} )", event.getTestVo().getName());
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
	public void afterRollback(TestEvent event) {
		log.info("## afterRollback");
		log.info("\t > location = {}", event.getLocation());
		log.info("\t > rollback testVo ( name = {} )", event.getTestVo().getName());
	}
	
}
