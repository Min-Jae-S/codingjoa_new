package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing // automatically registers some of its key components, such as JobBuilderFactory and StepBuilderFactory, as beans.
public class BatchConfig {
	
	// Spring Batch - Application, Batch Core, Batch Infrastrcuture
	// 	> Application 			: 개발자가 작성한 모든 배치 작업과 사용자 정의 코드 포함
	// 	> Batch Core  			: 배치 작업을 시작하고 제어하는데 필요한 핵심 런타임 클래스 포함 (JobLauncher, Job, Step)
	// 	> Batch Infrastructure	: 어플리케이션에서 사용하는 Tasklet, Reader, Processor, Writer 그리고 RetryTemplate과 같은 서비스를 포함
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	private DataSourceTransactionManager transactionManager;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ BatchConfig init");
		log.info("\t > dataSource = {}", dataSource);
//		log.info("\t > transactionManager = {}", transactionManager);
		log.info("===============================================================");
	}
	
	@Bean
	public Job batchJob() {
		return jobBuilderFactory.get("batchJob")
				.start(step1())
				.next(step2())
				.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet((contribution, chunkContext) -> {
					log.info("\t >>>>> step1");
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet((contribution, chunkContext) -> {
					log.info("\t >>>>> step2");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
}
