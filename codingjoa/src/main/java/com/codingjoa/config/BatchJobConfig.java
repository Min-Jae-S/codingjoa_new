package com.codingjoa.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
public class BatchJobConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job batchJob() {
		return jobBuilderFactory.get("batchJob")
				.start(step1())
				.next(step2())
				.listener(jobListener())
				.build();
	}
	
	@Bean
	@JobScope // late binding
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						log.info(">>> This is STEP1");
						return RepeatStatus.FINISHED;
					}
				})
				.allowStartIfComplete(true) // Step already complete or not restartable, so no action to execute
				.build();
	}

	@Bean
	@JobScope
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet((contribution, chunkContext) -> {
					log.info(">>> This is STEP2");
					return RepeatStatus.FINISHED;
				})
				.allowStartIfComplete(true)
				.build();
	}
	
	@Bean
	public JobExecutionListener jobListener() {
		return new JobExecutionListener() {
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info(">>> BEFORE JOB");
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info(">>> AFTER JOB");
			}
		};
	}
}
