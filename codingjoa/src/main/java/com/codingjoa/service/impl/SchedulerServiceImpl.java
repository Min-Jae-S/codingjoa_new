package com.codingjoa.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;
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
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String timestamp = LocalDateTime.now().format(formatter);
		String id = "smj" + RandomStringUtils.randomNumeric(4);
		
		TestSchedulerData sampleData = TestSchedulerData.builder()
				.id(id)
				.timestamp(timestamp)
				.build();
		log.info("\t > sampleData = {}", sampleData);
		
		int result = testSchedulerMapper.insert(sampleData);
		log.info("\t > result = {}", result);
	}
	
}
