package com.codingjoa.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ComponentScan(basePackages = "com.codingjoa.configurer")
@EnableBatchProcessing
@Slf4j
public class BatchConfig2 {
		
//	@Autowired
//	private JobBuilderFactory jobBuilderFactory;
//	
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
//
//	@Bean
//	public Job simpleJob() {
//		return jobBuilderFactory.get("simpleJob")
//				.start(step1())
//				.next(step2())
//				.build();
//	}
//	
//	@Bean
//	public Job batchJobA() {
//		return jobBuilderFactory.get("batchJobA")
//				.start(stepA1())
//				.next(stepA2())
//				.listener(jobListener())
//				.build();
//	}
//
//	@Bean
//	public Job batchJobB() {
//		return jobBuilderFactory.get("batchJobB")
//				.start(stepB1())
//				.next(stepB2())
//				.build();
//	}
//	
//	/* 
//	 * https://jojoldu.tistory.com/330
//	 * 
//	 * In Spring Batch, you can receive parameters from external or internal sources to use them across multiple Batch components. 
//	 * These parameters are called "Job Parameters", and to use job parameters, 
//	 * you must always declare a dedicated Spring Batch scope (@JobScope, @StepScope)
//	 * 
//	 * When you use @StepScope with Spring Batch components like Tasklet, ItemReader, ItemWriter, ItemProcessor, etc., 
//	 * Spring Batch creates these components as Spring Beans at the time of execution of the specified Step through the Spring container. 
//	 * Similarly, @JobScope causes the Bean to be created at the time of Job execution. 
//	 * In other words, it delays the creation of the Bean until the specified Scope is executed.
//	 * 
//	 * In some ways, it can be likened to the request scope in MVC. 
//	 * Just as the request scope is created when a request comes in and is discarded when the response is returned, 
//	 * JobScope and StepScope similarly involve the creation and deletion of beans when a Job or Step is executed and completed. 
//	 * 
//	 * The advantages gained by delaying the bean's creation until the execution of the Step or Job, 
//	 * as opposed to application startup, can be summarized into two main points.
//	 * 
//	 * 1. It enables Late Binding of JobParameters. 
//	 * This means that you can assign Job Parameters during the business logic processing stages, 
//	 * such as in Controllers or Services, even if it's not at the point when the application is initially executed.
//	 * 
//	 * 2. It proves useful when using the same component in parallel or concurrently. 
//	 * Imagine a scenario where you have a Tasklet within a Step, and this Tasklet has member variables and logic to modify these variables. 
//	 * Without @StepScope, when you run Steps in parallel, they might try to haphazardly modify the state of a single Tasklet. 
//	 * However, with @StepScope, each Step creates and manages its own separate Tasklet, 
//	 * ensuring there is no interference with each other's states.
//	 * 
//	 * Job Parameters can be accessed at the time of creating Step, Tasklet, Reader, 
//	 * and other Batch component beans, but specifically when creating Scope beans.
//	 * In other words, you can only use Job Parameters when creating @StepScope and @JobScope beans.
//	 * 
//	 */
//	
//	@Bean
//	@JobScope // Parameter Lazy Loading 
//	public Step step1() {
//		return stepBuilderFactory.get("step1")
//				.tasklet((contribution, chunkContext) -> {
//					log.info("## this is step1");
//					return RepeatStatus.FINISHED;
//				})
//				.allowStartIfComplete(true) // Step already complete or not restartable, so no action to execute
//				.build();
//	}
//	
//	@Bean
//	@JobScope
//	public Step step2() {
//		return stepBuilderFactory.get("step2")
//				.tasklet((contribution, chunkContext) -> {
//					log.info("## this is step2");
//					return RepeatStatus.FINISHED;
//				})
//				.allowStartIfComplete(true) 
//				.build();
//	}
//	
//	@Bean
//	@JobScope
//	public Step stepA1() {
//		return stepBuilderFactory.get("stepA1")
//				.tasklet((contribution, chunkContext) -> {
//					log.info("## this is stepA-1");
//					return RepeatStatus.FINISHED;
//				})
//				.allowStartIfComplete(true) 
//				.build();
//	}
//
//	@Bean
//	@JobScope
//	public Step stepA2() {
//		return stepBuilderFactory.get("stepA2")
//				.tasklet((contribution, chunkContext) -> {
//					log.info("## this is stepA-2");
//					return RepeatStatus.FINISHED;
//				})
//				.allowStartIfComplete(true)
//				.build();
//	}
//
//	@Bean
//	@JobScope 
//	public Step stepB1() {
//		return stepBuilderFactory.get("stepB1")
//				.tasklet((contribution, chunkContext) -> {
//					log.info("## this is stepB-1");
//					return RepeatStatus.FINISHED;
//				})
//				.allowStartIfComplete(true)
//				.build();
//	}
//	
//	@Bean
//	@JobScope
//	public Step stepB2() {
//		return stepBuilderFactory.get("stepB2")
//				.tasklet(new Tasklet() {
//					@Override
//					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//						log.info("## this is stepB-2");
//						return RepeatStatus.FINISHED;
//					}
//				})
//				.allowStartIfComplete(true)
//				.build();
//	}
//	
//	@Bean
//	public JobExecutionListener jobListener() {
//		return new JobExecutionListener() {
//			@Override
//			public void beforeJob(JobExecution jobExecution) {
//				log.info("## beforeJob");
//				log.info("\t > {}", jobExecution);
//			}
//			
//			@Override
//			public void afterJob(JobExecution jobExecution) {
//				log.info("## afterJob");
//				log.info("\t > {}", jobExecution);
//			}
//		};
//	}
}
