package com.codingjoa.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJobConfig {
		
	private final JobBuilderFactory jobBuilders;
	private final StepBuilderFactory stepBuilders;

	@Bean
	public JobExecutionListener jobListener() {
		return new JobExecutionListener() {
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("## beforeJob, jobExecutionId: {}", jobExecution.getId());
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info("## afterJob, jobExecutionId: {}", jobExecution.getId());
			}
		};
	}
}
