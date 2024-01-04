package com.codingjoa.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionEventService {
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
	public void alertTransactionStatus() {
		log.info("## TransactionEventService");
	}
}
