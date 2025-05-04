package com.codingjoa.config;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.codingjoa.batch.FileDeletingItemWriter;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ComponentScan("com.codingjoa.batch")
@Configuration
public class BatchJobConfig {
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;
	
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
	
	/******************************************************************************************/
	/******************************************************************************************/
	
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
	
	/******************************************************************************************/ 
	/******************************************************************************************/ 
	
	@Bean 
	public Job taskletJob(@Qualifier("taskletStep") Step taskletStep) {
		return jobBuilderFactory.get("taskletJob")
				.start(taskletStep)
				.build();
	}
	
	@Bean
	public Step taskletStep(@Qualifier("simpleTasklet") Tasklet simpleTasklet) {
		return stepBuilderFactory.get("taskletStep")
				.tasklet(simpleTasklet)
				.build();
	}
	

	/******************************************************************************************/
	/******************************************************************************************/
	
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

	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public Job myBatisJob() {
		return jobBuilderFactory.get("myBatisJob")
				.start(myBatisStep())
				.build();
	}

	@Bean
	public Step myBatisStep() {
		return stepBuilderFactory.get("myBatisStep")
				.<User, Long>chunk(10)
				.reader(myBatisItemReader())
				.processor(myBatisItemProcessor())
				.writer(myBatisItemWriter())
				.build();
	}
	
	@Bean
	public MyBatisPagingItemReader<User> myBatisItemReader() {
		return new MyBatisPagingItemReaderBuilder<User>()
				.sqlSessionFactory(sqlSessionFactory)
				.queryId("com.codingjoa.mapper.BatchMapper.test")
				.pageSize(10)
				.maxItemCount(100)
				.build();
	}

	@Bean
	public ItemProcessor<User, Long> myBatisItemProcessor() {
		return user -> {
			Long userId = user.getId();
			log.info("## myBatisItemProcessor.process: {}", userId);
			return userId;
		};
	}
	
	@Bean
	public ItemWriter<Long> myBatisItemWriter() {
		return userIds -> {
			log.info("## mybatisItemWriter.write: {}", userIds);
		};
	}
	
	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public Job boardImagesCleanupJob() {
		return jobBuilderFactory.get("boardImagesCleanupJob")
				.start(boardImagesCleanupStep())
				.build();
	}

	@Bean
	public Step boardImagesCleanupStep() {
		return stepBuilderFactory.get("boardImagesCleanupStep")
				.<BoardImage, Long>chunk(10)
				.reader(boardImagesCleanupReader())
				.writer(boardImagesCleanupCompositeWriter())
				.build();
	}
	
	@StepScope
	@Bean
	public MyBatisPagingItemReader<BoardImage> boardImagesCleanupReader() {
		return new MyBatisPagingItemReaderBuilder<BoardImage>()
				.sqlSessionFactory(sqlSessionFactory)
				.queryId("com.codingjoa.mapper.BatchMapper.findOrphanBoardImages")
				.pageSize(10)
				.build();
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Bean
	public CompositeItemWriter<Object> boardImagesCleanupCompositeWriter() {
		List<ItemWriter> writers = List.of(boardImagesCleanupDbWriter(), boardImagesCleanupFileWriter());
		CompositeItemWriter compositeWriter = new CompositeItemWriter();
		compositeWriter.setDelegates(writers);
		return compositeWriter;
	}
	
	@StepScope
	@Bean
	public MyBatisBatchItemWriter<Long> boardImagesCleanupDbWriter() {
		return null;
	}

	@StepScope
	@Bean
	public FileDeletingItemWriter<BoardImage> boardImagesCleanupFileWriter() {
		return new FileDeletingItemWriter<BoardImage>();
	}
	
	
}
