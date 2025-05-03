package com.codingjoa.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.aop.support.AopUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
	public Job multiStepsJob() {
		return jobBuilderFactory.get("multiStepsJob")
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
	public Step chunkStep1(@Qualifier("itemReader1") ItemReader<String> itemReader, 
			@Qualifier("itemWriter1") ItemWriter<String> itemWriter) {
		log.info("## create chunkStep1 bean");
		log.info("\t > itemReader = {}", itemReader.getClass().getName());
		log.info("\t > itemWriter = {}", itemWriter.getClass().getName());
		
		return stepBuilderFactory.get("chunkStep1")
				.<String, String>chunk(10)
				.reader(itemReader)
				.writer(itemWriter)
				.build();
	}

	@Bean
	public Step chunkStep2(@Qualifier("itemReader2") ItemReader<String> itemReader, 
			@Qualifier("itemWriter2") ItemWriter<String> itemWriter) {
		log.info("## create chunkStep2 bean");
		log.info("\t > itemReader = {}", itemReader.getClass().getName());
		log.info("\t > itemWriter = {}", itemWriter.getClass().getName());
		
		return stepBuilderFactory.get("chunkStep")
				.<String, String>chunk(10)
				.reader(itemReader)
				.writer(itemWriter)
				.build();
	}
	
	@StepScope
	@Bean
	public ListItemReader<String> itemReader1(@Value("#{jobParameters['lastNamesStr']}") String lastNamesStr) {
		log.info("## create itemReader1 bean");
		List<String> lastNames = (lastNamesStr != null) ? Arrays.stream(lastNamesStr.split(",")).collect(Collectors.toList()) : List.of();
		
		return new ListItemReader<String>(lastNames) {
			
			private StepExecution stepExecution;
			
			@BeforeStep
			public void saveStepExecution(StepExecution stepExecution) {
				log.info("## itemReader1.saveStepExecution");
				this.stepExecution = stepExecution;
			}
			
			@Override
			public String read() {
				log.info("## itemReader1.read");
				log.info("\t > jobParameters = {}", stepExecution != null ? stepExecution.getJobParameters() : null);
				String item = super.read();
				log.info("\t > item = {}", item);
				return item;
			}
		};
	}
	
	@Bean
	public ListItemReader<String> itemReader2() {
		log.info("## create itemReader2 bean");
		List<String> lastNames = List.of("seo", "lee");
		return new ListItemReader<String>(lastNames) {
			
			private StepExecution stepExecution;
			
			@BeforeStep
			public void beforeStep(StepExecution stepExecution) {
				log.info("## itemReader2.beforeStep");
				this.stepExecution = stepExecution;
			}

			@AfterStep
			public void afterStep(StepExecution stepExecution) {
				log.info("## itemReader2.afterStep");
			}
			
			@Override
			public String read() {
				log.info("## itemReader2.read");
				log.info("\t > jobParameters = {}", stepExecution != null ? stepExecution.getJobParameters() : null);
				String item = super.read();
				log.info("\t > item = {}", item);
				return item;
			}
		};
	}
	
//	@StepScope
//	@Bean
//	public ItemProcessor<String, String> itemProcessor() {
//		log.info("## {}.itemProcessor", this.getClass().getSimpleName());
//		return item -> {
//			String processedItem = item.toUpperCase();
//			log.info("## ItemProcessor.process: {}", processedItem);
//			return processedItem;
//		};
//	}
	
	@StepScope
	@Bean
	public ListItemWriter<String> itemWriter1() {
		log.info("## create itemWriter1 bean");
		return new ListItemWriter<String>() {
			
			@Override
			public void write(List<? extends String> items) throws Exception {
				log.info("## itemWriter1.write: {}", items);
			}
		};
	}

	@Bean
	public ListItemWriter<String> itemWriter2() {
		log.info("## create itemWriter2 bean");
		return new ListItemWriter<String>() {
			
			@Override
			public void write(List<? extends String> items) throws Exception {
				log.info("## itemWriter2.write: {}", items);
			}
		};
	}
	
}
