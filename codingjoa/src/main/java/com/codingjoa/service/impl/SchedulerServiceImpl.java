package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.mapper.test.TestSchedulerMapper;
import com.codingjoa.service.SchedulerService;
import com.codingjoa.test.TestSchedulerData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerServiceImpl implements SchedulerService {
	
	private final TestSchedulerMapper testSchedulerMapper;
	
	@Override
	public void execute() {
		log.info("## {}.execute", this.getClass().getSimpleName());
		log.info("\t > transaction active = {}", TransactionSynchronizationManager.isActualTransactionActive());
	}

	@Transactional
	@Override
	public void insert() {
		log.info("## {}.insert", this.getClass().getSimpleName());
		log.info("\t > transaction active = {}", TransactionSynchronizationManager.isActualTransactionActive());
		
		TestSchedulerData testSchedulerData = TestSchedulerData.builder()
				.id(null)
				.pw(null)
				.build();
		log.info("\t > testSchedulerData = {}", testSchedulerData);
		
		int result = testSchedulerMapper.insert(testSchedulerData);
		log.info("\t > result = {}", result);
	}
	
}
