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
	public void afterCommit(TestVo testVo) {
		log.info("## afterCommit");
		log.info("\t > commit testVo ( id = {}, name = {} )", testVo.getId(), testVo.getName());
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
	public void afterRollback(TestVo testVo) {
		log.info("## afterRollback");
		log.info("\t > rollback testVo ( id = {}, name = {} )", testVo.getId(), testVo.getName());
	}
	
}
