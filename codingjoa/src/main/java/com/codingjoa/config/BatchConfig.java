package com.codingjoa.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
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

	// https://stackoverflow.com/questions/65607909/spring-batch-2-4-1-wildfly-20-final-java-lang-nosuchfielderror-block-unsafe
	// Caused by: java.lang.NoSuchFieldError: BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES
	// springbatch 4.3.0 has introduced a dependency on jackson databind 2.11.
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
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
				.tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						log.info("\t >>>>> STEP1");
						return RepeatStatus.FINISHED;
					}
				})
				.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet((contribution, chunkContext) -> {
					log.info("\t >>>>> STEP2");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
}
