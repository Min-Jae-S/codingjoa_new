package com.codingjoa.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.codingjoa.test.TestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionEventService {
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void alertAfterCommit(TestVo testVo) {
		log.info("## TransactionEventService");
		log.info("\t > commit '{}'", testVo.getId());
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
	public void alertAfterRollback(TestVo testVo) {
		log.info("## TransactionEventService");
		log.info("\t > rollback '{}'", testVo.getId());
	}
	
}
