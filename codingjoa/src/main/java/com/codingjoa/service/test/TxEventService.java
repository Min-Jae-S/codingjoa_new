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
	public void alertAfterCommit(TestEvent event) {
		log.info("## alertAfterCommit");
		log.info("\t > location = {}", event.getLocation());
		log.info("\t > commit testVo ( name = {} )", event.getTestVo().getName());
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
	public void alertAfterRollback(TestEvent event) {
		log.info("## alertAfterRollback");
		log.info("\t > location = {}", event.getLocation());
		log.info("\t > rollback testVo ( name = {} )", event.getTestVo().getName());
	}
	
}
