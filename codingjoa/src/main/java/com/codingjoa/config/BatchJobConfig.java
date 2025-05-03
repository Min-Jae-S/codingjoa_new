package com.codingjoa.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.aop.support.AopUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.codingjoa.batch.ItemReader1;
import com.codingjoa.batch.ItemReader2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@ComponentScan("com.codingjoa.batch")
@Configuration
public class BatchJobConfig {
		
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final Tasklet tasklet;
	
	@Bean 
	public Job multiStepsJob() {
		return jobBuilderFactory.get("multiStepsJob")
				.start(firstStep())
				.next(middleStep())
				.next(lastStep())
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
	public Job flowJob() {
		return jobBuilderFactory.get("flowJob")
				.start(entryStep())
					.on(ExitStatus.COMPLETED.getExitCode()).to(successStep())
					.on("*").to(alwaysStep())
				.from(entryStep())
					.on(ExitStatus.FAILED.getExitCode()).to(failureStep())
					.on("*").to(alwaysStep())
				.from(entryStep())
					.on("*").to(alwaysStep())
				.end()
				.build();
	}
	
	@Bean
	public Step entryStep() {
		return stepBuilderFactory.get("entryStep")
				.tasklet((contribution, chunkContext) -> {
					log.info("## entryStep");

					Map<String, Object> params = chunkContext.getStepContext().getJobParameters();
					String flowStatusParam = (String) params.get("flowStatus");
					Boolean flowStatus = (flowStatusParam != null) ? Boolean.valueOf(flowStatusParam) : null;
					log.info("\t > flowStaus = {}", flowStatus);

					if (flowStatus == null) {
						contribution.setExitStatus(ExitStatus.UNKNOWN);
					} else if (flowStatus) {
						contribution.setExitStatus(ExitStatus.COMPLETED);
					} else {
						contribution.setExitStatus(ExitStatus.FAILED);
					}
					
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	@Bean
	public Step successStep() {
		return stepBuilderFactory.get("successStep")
				.tasklet((contribution, chunkContext) -> {
					log.info("## successStep");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	@Bean
	public Step failureStep() {
		return stepBuilderFactory.get("failureStep")
				.tasklet((contribution, chunkContext) -> {
					log.info("## failureStep");
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	@Bean
	public Step alwaysStep() {
		return stepBuilderFactory.get("alwaysStep")
				.tasklet((contribution, chunkContext) -> {
					log.info("## alwaysStep");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	@Bean 
	public Job taskletJob() {
		return jobBuilderFactory.get("taskletJob")
				.start(taskletStep())
				.build();
	}
	
	@Bean
	public Step taskletStep() {
		return stepBuilderFactory.get("taskletStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean 
	public Job chunkJob1(@Qualifier("chunkStep1") Step chunkStep) {
		return jobBuilderFactory.get("chunkJob1")
				.start(chunkStep)
				.build();
	}

	@Bean 
	public Job chunkJob2(@Qualifier("chunkStep2") Step chunkStep) {
		return jobBuilderFactory.get("chunkJob2")
				.start(chunkStep)
				.build();
	}
	
	@Bean
	public Step chunkStep1(@Qualifier("itemReader1") ItemReader<String> itemReader) {
		return stepBuilderFactory.get("chunkStep1")
				.<String, String>chunk(10)
				.reader(itemReader)
				.processor(itemProcessor())
				.writer(itemWriter())
				.build();
	}

	@Bean
	public Step chunkStep2(@Qualifier("itemReader2") ItemReader<String> itemReader) {
		return stepBuilderFactory.get("chunkStep")
				.<String, String>chunk(10)
				.reader(itemReader)
				.processor(itemProcessor())
				.writer(itemWriter())
				.build();
	}
	
	@Bean
	public ItemProcessor<String, String> itemProcessor() {
		return item -> {
			String processedItem = item.toUpperCase();
			log.info("## ItemProcessor.process: {}", processedItem);
			return processedItem;
		};
	}
	
	@Bean
	public ItemWriter<String> itemWriter() {
		return items -> {
			log.info("## ItemWriter.write: {}", items);
		};
	}

	
}
