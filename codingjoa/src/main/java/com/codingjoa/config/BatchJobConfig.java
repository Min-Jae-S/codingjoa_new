package com.codingjoa.config;

import java.util.List;
import java.util.Map;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
					Map<String, Object> params = chunkContext.getStepContext().getJobParameters();
					String flowStatusParam = (String) params.get("flowStatus");
					Boolean flowStatus = (flowStatusParam != null) ? Boolean.valueOf(flowStatusParam) : null;
					log.info("## entryStep, flowStatus = {}", flowStatus);

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
	public Job chunkJob(@Qualifier("chunkStep") Step chunkStep) {
		return jobBuilderFactory.get("chunkJob")
				.start(chunkStep)
				.build();
	}
	
	@Bean
	public Step chunkStep(ItemReader<String> itemReader, ItemProcessor<String, String> itemProcessor, ItemWriter<String> itemWriter) {
		return stepBuilderFactory.get("chunkStep")
				.<String, String>chunk(10)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.build();
	}
	
	@StepScope
	@Bean
	public ListItemReader<String> itemReader(JobParameters jobParameters) {
		log.info("## {}.itemReader", this.getClass().getSimpleName());
		List<String> items = List.of("kim", "lee", "park", "choi", "jung", "yoon", "han");
		return new ListItemReader<String>(items) {
			
			@BeforeStep
			public void beforeStep(StepExecution stepExecution) {
				log.info("## ListItemWriter.beforeStep");
			}
			
			@AfterStep
			public void afterStep(StepExecution stepExecution) {
				log.info("## ListItemWriter.afterStep");
			}
			
			@Override
			public String read() {
				String item = super.read();
				log.info("## [ListItemReader] retrieved item: {}", item);
				return item;
			}
		};
	}
	
	@StepScope
	@Bean
	public ItemProcessor<String, String> itemProcessor() {
		log.info("## {}.itemProcessor", this.getClass().getSimpleName());
		return item -> {
			String processedItem = item.toUpperCase();
			//log.info("## [ItemProcessor] proccessed item: {}", processedItem);
			return processedItem;
		};
	}
	
	@StepScope
	@Bean
	public ItemWriter<String> itemWriter() {
		log.info("## {}.itemWriter", this.getClass().getSimpleName());
		return new ListItemWriter<String>() {
			@BeforeStep
			public void beforeStep(StepExecution stepExecution) {
				log.info("## ListItemWriter.beforeStep");
			}
			
			@AfterStep
			public void afterStep(StepExecution stepExecution) {
				log.info("## ListItemWriter.afterStep");
			}
			
			@Override
			public void write(List<? extends String> items) throws Exception {
				log.info("## ListItemWriter, writing items: {}", items);
			}
		};
	}
	
}
