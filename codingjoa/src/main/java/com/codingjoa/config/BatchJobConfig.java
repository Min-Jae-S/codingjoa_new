package com.codingjoa.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.util.UriComponentsBuilder;

import com.codingjoa.batch.BoardImageCleanupListener;
import com.codingjoa.batch.BoardImageFileItemWriter;
import com.codingjoa.batch.MybatisRecentKeysetPagingItemReader;
import com.codingjoa.batch.PermissiveSkipPolicy;
import com.codingjoa.dto.BoardCountsCorrection;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@ComponentScan("com.codingjoa.batch")
@RequiredArgsConstructor
@Configuration
public class BatchJobConfig {
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;
	private final PlatformTransactionManager transactionManager;
	private final Environment env;
	
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
		return userIds -> log.info("## mybatisItemWriter.write: {}", userIds);
	}
	
	/******************************************************************************************/
	/******************************************************************************************/

	@Bean
	public Job boardImageDummyJob(@Qualifier("boardImageDummyStep") Step boardImageDummyStep) {
		return jobBuilderFactory.get("boardImageDummyJob")
				.start(boardImageDummyStep)
				.build();
	}
	
	@Bean
	public Step boardImageDummyStep(@Qualifier("boardImageDummyReader") ItemReader<BoardImage> boardImageDummyReader, 
			@Qualifier("boardImageDummyWriter") ItemWriter<BoardImage> boardImageDummyWriter) {
		return stepBuilderFactory.get("boardImageDummyStep")
				.transactionManager(transactionManager)
				.<BoardImage, BoardImage>chunk(100)
				.reader(boardImageDummyReader)
				.writer(boardImageDummyWriter)
				.build();	
	}
	
	@StepScope
	@Bean
	public ListItemReader<BoardImage> boardImageDummyReader(@Value("#{jobParameters['boardImageDir']}") String boardImageDir) {
		File folder = new File(boardImageDir);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		Resource resource = new ClassPathResource("static/dummy_base.jpg");
		
		List<BoardImage> dummyImages = new ArrayList<>();
		
		for (int i = 1; i <= 200; i++) {
			String filename = "dummy_" + UUID.randomUUID() + ".jpg";
			File copyFile = new File(folder, filename);
			
			try (InputStream in = resource.getInputStream()) {
				Files.copy(in, copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				log.info("## {}: {}", e.getClass().getSimpleName(), e.getMessage());
			}

			String path = UriComponentsBuilder.fromPath("/board/images/{filename}")
					.buildAndExpand(filename)
					.toUriString();
			
			BoardImage boardImage = BoardImage.builder()
					.boardId(null)
					.name(filename)
					.path(path)
					.build();
			dummyImages.add(boardImage);
		}
		
		return new ListItemReader<>(dummyImages);
	}

	@Bean
	public ItemWriter<BoardImage> boardImageDummyWriter() {
		MyBatisBatchItemWriter<BoardImage> writer = new MyBatisBatchItemWriter<>();
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.insertBoardImageDummy");
		return writer;
	}
	
	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public Job boardImageCleanupJob() {
		return jobBuilderFactory.get("boardImageCleanupJob")
				.start(boardImageCleanupStep())
				.build();
	}

	@Bean
	public Step boardImageCleanupStep() {
		return stepBuilderFactory.get("boardImageCleanupStep")
				.transactionManager(transactionManager)
				.<BoardImage, BoardImage>chunk(10)
				.reader(boardImageItemReader())
				.writer(compositeBoardImageItemWriter())
				.faultTolerant()
				.skipPolicy(new PermissiveSkipPolicy())
				.listener(boardImageCleanupListener())
				.build();
	}
	
	// [WARN ]  o.s.b.c.l.AbstractListenerFactoryBean    : org.springframework.batch.item.ItemReader is an interface. 
	// The implementing class will not be queried for annotation based listener configurations.
	// If using @StepScope on a @Bean method, be sure to return the implementing class so listener annotations can be used.
	//@StepScope
	@Bean
	public MybatisRecentKeysetPagingItemReader<BoardImage> boardImageItemReader() {
		MybatisRecentKeysetPagingItemReader reader = new MybatisRecentKeysetPagingItemReader<BoardImage>();
		reader.setSqlSessionFactory(sqlSessionFactory);
		reader.setQueryId("com.codingjoa.mapper.BatchMapper.findOrphanBoardImages");
		reader.setPageSize(10);
		return reader;
	}
	
	@Bean
	public CompositeItemWriter<BoardImage> compositeBoardImageItemWriter() {
		return new CompositeItemWriterBuilder()
				.delegates(boardImageItemWriter(), boardImageFileItemWriter())
				.build();
	}
	
	public MyBatisBatchItemWriter<BoardImage> boardImageItemWriter() {
		MyBatisBatchItemWriter<BoardImage> writer = new MyBatisBatchItemWriter<>() {
			@Override
			public void write(List<? extends BoardImage> items) {
				log.info("## MyBatisBatchItemWriter.write");
				List<Long> ids = items.stream()
						.map(item -> ((BoardImage)item).getId())
						.collect(Collectors.toList());
				log.info("\t > items: {}", ids);
				super.write(items);
			}
		};
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.deleteBoardImage");
		return writer;
	}
	
	public BoardImageFileItemWriter boardImageFileItemWriter() {
		BoardImageFileItemWriter writer = new BoardImageFileItemWriter();
		writer.setBoardImageDir(env.getProperty("upload.dir.board.image"));
		return writer;
	}
	
	@Bean
	public BoardImageCleanupListener boardImageCleanupListener() {
		return new BoardImageCleanupListener();
	}

	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public Job boardCountsCorrectionJob() {
		return jobBuilderFactory.get("boardCountsCorrectionJob")
				.start(boardCountsCorrectionStep())
				.build();
	}

	@Bean
	public Step boardCountsCorrectionStep() {
		return stepBuilderFactory.get("boardCountsCorrectionStep")
				.transactionManager(transactionManager)
				.<BoardCountsCorrection, BoardCountsCorrection>chunk(10)
				.reader(boardCountsCorrectionReader())
				.writer(boardCountsCorrectionWriter())
				.faultTolerant()
				.skipPolicy(new PermissiveSkipPolicy())
				.build();
	}
	
	@Bean
	public MyBatisPagingItemReader<BoardCountsCorrection> boardCountsCorrectionReader() {
		MyBatisPagingItemReader reader = new MyBatisPagingItemReader<BoardCountsCorrection>();
		reader.setSqlSessionFactory(sqlSessionFactory);
		reader.setQueryId("com.codingjoa.mapper.BatchMapper.findBoardCountsCorrection");
		reader.setPageSize(10);
		return reader;
	}

	@Bean
	public ItemWriter<BoardCountsCorrection> boardCountsCorrectionWriter() {
		MyBatisBatchItemWriter writer = new MyBatisBatchItemWriter<BoardCountsCorrection>() {
			@Override
			public void write(List<? extends BoardCountsCorrection> items) {
				log.info("## MyBatisBatchItemWriter.write");
				items.stream().forEach(boardCountsCorrection -> 
					log.info("\t > boardId: {}, realCommentCount: {}, realLikeCount: {}", boardCountsCorrection.getBoardId(),
							 boardCountsCorrection.getRealCommentCount(), boardCountsCorrection.getRealLikeCount())
				);
				super.write(items);
			}
		};
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.updateBoardCountsCorrection");
		return writer;
	}
	
	
	
}
