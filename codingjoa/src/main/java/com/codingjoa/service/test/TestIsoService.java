package com.codingjoa.service.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestIsoService {
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void isoReadCommitted() {
		log.info("## isoReadCommitted");
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void isoReadUncommitted() {
		log.info("## isoReadUncommitted");
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void isoRepeatableRead() {
		log.info("## isoRepeatableRead");
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void isoSerializable() {
		log.info("## isoSerializable");
	}
}
