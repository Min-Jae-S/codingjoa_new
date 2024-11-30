package com.codingjoa.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
	public void insert(String jobName) {
		log.info("## {}.insert", this.getClass().getSimpleName());
		log.info("\t > transaction active = {}", TransactionSynchronizationManager.isActualTransactionActive());
		
		TestSchedulerData sampleData = createSampleData(jobName);
		log.info("\t > sampleData = {}", sampleData);
		
		int result = testSchedulerMapper.insert(sampleData);
		log.info("\t > result = {}", result);
	}
	
	@Transactional
	@Override
	public void insertOnException(String jobName) {
		log.info("## {}.insertOnException", this.getClass().getSimpleName());
		log.info("\t > transaction active = {}", TransactionSynchronizationManager.isActualTransactionActive());
		
		TestSchedulerData sampleData = createSampleData(jobName);
		log.info("\t > sampleData = {}", sampleData);
		
		int result = testSchedulerMapper.insert(sampleData);
		log.info("\t > result = {}", result);
		
		log.info("\t > thorw runtime exception");
		throw new RuntimeException("insertOnException");
	}
	
	private TestSchedulerData createSampleData(String jobName) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String timestamp = LocalDateTime.now().format(formatter);
		return TestSchedulerData.builder()
				.id("smj" + RandomStringUtils.randomNumeric(4))
				.jobName(jobName)
				.timestamp(timestamp)
				.build();
	}

	@Override
	public List<TestSchedulerData> getSampleData() {
		return testSchedulerMapper.findSampleData();
	}
	
}
