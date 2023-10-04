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
	public Job batchJobA() {
		log.info("## batchJobA");
		return jobBuilderFactory.get("batchJobA")
				.start(stepA1())
				.next(stepA2())
				.listener(jobListener())
				.build();
	}

	@Bean
	public Job batchJobB() {
		log.info("## batchJobB");
		return jobBuilderFactory.get("batchJobB")
				.start(stepB1())
				.next(stepB2())
				.build();
	}
	
	@Bean
	@JobScope // late binding
	public Step stepA1() {
		log.info("## stepA1 - late binding");
		return stepBuilderFactory.get("stepA1")
				.tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						log.info("## this is stepA1");
						return RepeatStatus.FINISHED;
					}
				})
				.allowStartIfComplete(true) // Step already complete or not restartable, so no action to execute
				.build();
	}

	@Bean
	@JobScope
	public Step stepA2() {
		log.info("## stepA2 - late binding");
		return stepBuilderFactory.get("stepA2")
				.tasklet((contribution, chunkContext) -> {
					log.info("## this is stepA2");
					return RepeatStatus.FINISHED;
				})
				.allowStartIfComplete(true)
				.build();
	}

	@Bean
	@JobScope 
	public Step stepB1() {
		log.info("## stepB1 - late binding");
		return stepBuilderFactory.get("stepB1")
				.tasklet((contribution, chunkContext) -> {
					log.info("## this is stepB1");
					return RepeatStatus.FINISHED;
				})
				.allowStartIfComplete(true)
				.build();
	}
	
	@Bean
	@JobScope
	public Step stepB2() {
		log.info("## stepB2 - late binding");
		return stepBuilderFactory.get("stepB2")
				.tasklet((contribution, chunkContext) -> {
					log.info("## this is stepB2");
					return RepeatStatus.FINISHED;
				})
				.allowStartIfComplete(true)
				.build();
	}
	
	@Bean
	public JobExecutionListener jobListener() {
		log.info("# JobExecutionListener");
		return new JobExecutionListener() {
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("## beforeJob");
				log.info("\t > {}", jobExecution);
			}
			
			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info("## afterJob");
				log.info("\t > {}", jobExecution);
			}
		};
	}
}
