package com.codingjoa.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJobConfig {
		
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public JobExecutionListener jobListener() {
		return new JobExecutionListener() {
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("## beforeJob");
				log.info("\t > jobExecution = {}", jobExecution);
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info("## afterJob");
				log.info("\t > jobExecution = {}", jobExecution);
			}
		};
	}
	
	@Bean 
	public Job exampleJob1() {
		return jobBuilderFactory.get("exampleJob1")
				.start(firstStep())
				.next(middleStep())
				.next(lastStep())
				//.listener(jobListener())
				.build();
	}
	
	@Bean
	public Step firstStep() {
		return stepBuilderFactory.get("firstStep")
				.tasklet((contribution, chunkContext) -> {
					log.info("## first step ({})", Thread.currentThread().getName());
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	@Bean
	public Step middleStep() {
		return stepBuilderFactory.get("middleStep")
				.tasklet((contribution, chunkContext) -> {
					log.info("## middle step ({})", Thread.currentThread().getName());
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	@Bean
	public Step lastStep() {
		return stepBuilderFactory.get("lastStep")
				.tasklet((contribution, chunkContext) -> {
					log.info("## last step ({})", Thread.currentThread().getName());
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	@Bean 
	public Job exampleJob2() {
		return jobBuilderFactory.get("exampleJob2")
				.start(firstStep())
				.next(middleStep())
				.next(lastStep())
				//.listener(jobListener())
				.build();
	}

	
}
