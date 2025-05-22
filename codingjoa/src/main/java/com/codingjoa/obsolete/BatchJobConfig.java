package com.codingjoa.obsolete;

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

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
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

import com.codingjoa.batch.BoardCountColumn;
import com.codingjoa.batch.BoardImageFileItemWriter;
import com.codingjoa.batch.CommentCountColumn;
import com.codingjoa.batch.MybatisRecentKeysetPagingItemReader;
import com.codingjoa.batch.PermissiveSkipPolicy;
import com.codingjoa.batch.SkippedIdCatchListener;
import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.User;
import com.codingjoa.entity.UserImage;

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
	
	/* 
	 * @@ https://jojoldu.tistory.com/330
	 * 
	 * In Spring Batch, you can receive parameters from external or internal sources to use them across multiple Batch components. 
	 * These parameters are called "Job Parameters", and to use job parameters, 
	 * you must always declare a dedicated Spring Batch scope (@JobScope, @StepScope)
	 * 
	 * When you use @StepScope with Spring Batch components like Tasklet, ItemReader, ItemWriter, ItemProcessor, etc., 
	 * Spring Batch creates these components as Spring Beans at the time of execution of the specified Step through the Spring container. 
	 * Similarly, @JobScope causes the Bean to be created at the time of Job execution. 
	 * In other words, it delays the creation of the Bean until the specified Scope is executed.
	 * 
	 * In some ways, it can be likened to the request scope in MVC. 
	 * Just as the request scope is created when a request comes in and is discarded when the response is returned, 
	 * JobScope and StepScope similarly involve the creation and deletion of beans when a Job or Step is executed and completed. 
	 * 
	 * The advantages gained by delaying the bean's creation until the execution of the Step or Job, 
	 * as opposed to application startup, can be summarized into two main points.
	 * 
	 * 1. It enables Late Binding of JobParameters. 
	 * This means that you can assign Job Parameters during the business logic processing stages, 
	 * such as in Controllers or Services, even if it's not at the point when the application is initially executed.
	 * 
	 * 2. It proves useful when using the same component in parallel or concurrently. 
	 * Imagine a scenario where you have a Tasklet within a Step, and this Tasklet has member variables and logic to modify these variables. 
	 * Without @StepScope, when you run Steps in parallel, they might try to haphazardly modify the state of a single Tasklet. 
	 * However, with @StepScope, each Step creates and manages its own separate Tasklet, 
	 * ensuring there is no interference with each other's states.
	 * 
	 * Job Parameters can be accessed at the time of creating Step, Tasklet, Reader, 
	 * and other Batch component beans, but specifically when creating Scope beans.
	 * In other words, you can only use Job Parameters when creating @StepScope and @JobScope beans.
	 * 
	 */
	
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
	
	@StepScope // Parameter Lazy Loading 
	@Bean
	public ListItemReader<BoardImage> boardImageDummyReader(@Value("#{jobParameters['boardImageDir']}") String boardImageDir) {
		File folder = new File(boardImageDir);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		Resource resource = new ClassPathResource("static/dummy_base.jpg");
		
		List<BoardImage> dummyImages = new ArrayList<>();
		final int MAX = 30;
		for (int i = 1; i <= MAX; i++) {
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
	
	@Bean
	public Job userImageDummyJob(@Qualifier("userImageDummyStep") Step userImageDummyStep) {
		return jobBuilderFactory.get("userImageDummyJob")
				.start(userImageDummyStep)
				.build();
	}
	
	@Bean
	public Step userImageDummyStep(@Qualifier("userImageDummyTaskelet") Tasklet userImageDummyTaskelet) {
		return stepBuilderFactory.get("userImageDummyStep")
				.transactionManager(transactionManager)
				.tasklet(userImageDummyTaskelet)
				.build();	
	}
	
	@SuppressWarnings("resource")
	@StepScope
	@Bean
	public Tasklet userImageDummyTaskelet(@Value("#{jobParameters['userImageDir']}") String userImageDir) {
		return (contribution, chunkContext) -> {
			log.info("## UserImageDummyTaskelet.execute");
			
			SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
			sqlSessionTemplate.update("com.codingjoa.mapper.BatchMapper.resetUserImageAllLatestFlag");
				
			File folder = new File(userImageDir);
			if (!folder.exists()) {
				folder.mkdirs();
			}
				
			Resource resource = new ClassPathResource("static/dummy_base.jpg");
			List<Long> userIds = List.of(1L, 6021L, 6041L);
			int MAX = 10;
			
			for (Long userId: userIds) {
				for (int i = 1; i <= MAX; i++) {
					String filename = "dummy_" + UUID.randomUUID() + ".jpg";
					File copyFile = new File(folder, filename);
					
					try (InputStream in = resource.getInputStream()) {
						Files.copy(in, copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						log.info("## {}: {}", e.getClass().getSimpleName(), e.getMessage());
					}
					
					String path = UriComponentsBuilder.fromPath("/user/images/{filename}")
							.buildAndExpand(filename)
							.toUriString();
					
					UserImage userImage = UserImage.builder()
							.userId(userId)
							.name(filename)
							.path(path)
							.latest(i == MAX)
							.build();
					
					sqlSessionTemplate.update("com.codingjoa.mapper.BatchMapper.insertUserImageDummy", userImage);
				}
			}
			
			sqlSessionTemplate.flushStatements();
			return RepeatStatus.FINISHED;
		};
	}
	
	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public SkippedIdCatchListener skippedIdCatchListener() {
		return new SkippedIdCatchListener();
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
				.listener(skippedIdCatchListener())
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
		reader.enableFixedPage(0);
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
				log.info("## BoardImageItemWriter.write");
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
	
	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public Job userImageCleanupJob() {
		return jobBuilderFactory.get("userImageCleanupJob")
				.start(boardImageCleanupStep())
				.build();
	}

	@Bean
	public Step userImageCleanupStep() {
		return stepBuilderFactory.get("userImageCleanupStep")
				.transactionManager(transactionManager)
				.<UserImage, UserImage>chunk(10)
				.reader(userImageItemReader())
				.writer(compositeUserImageItemWriter())
				.faultTolerant()
				.skipPolicy(new PermissiveSkipPolicy())
				.listener(skippedIdCatchListener())
				.build();
	}
	
	@Bean
	public MybatisRecentKeysetPagingItemReader<UserImage> userImageItemReader() {
		MybatisRecentKeysetPagingItemReader reader = new MybatisRecentKeysetPagingItemReader<UserImage>();
		reader.setSqlSessionFactory(sqlSessionFactory);
		reader.setQueryId("com.codingjoa.mapper.BatchMapper.findOrphanUserImages");
		reader.setPageSize(10);
		reader.enableFixedPage(0);
		return reader;
	}
	
	@Bean
	public CompositeItemWriter<UserImage> compositeUserImageItemWriter() {
		return new CompositeItemWriterBuilder()
				.delegates(userImageItemWriter(), userImageFileItemWriter())
				.build();
	}
	
	public MyBatisBatchItemWriter<UserImage> userImageItemWriter() {
		MyBatisBatchItemWriter<UserImage> writer = new MyBatisBatchItemWriter<>() {
			@Override
			public void write(List<? extends UserImage> items) {
				log.info("## UserImageItemWriter.write");
				List<Long> ids = items.stream()
						.map(item -> ((UserImage)item).getId())
						.collect(Collectors.toList());
				log.info("\t > items: {}", ids);
				super.write(items);
			}
		};
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.deleteUserImage");
		return writer;
	}
	
	public BoardImageFileItemWriter userImageFileItemWriter() {
		BoardImageFileItemWriter writer = new BoardImageFileItemWriter();
		writer.setBoardImageDir(env.getProperty("upload.dir.user.image"));
		return writer;
	}

	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public Job boardCountColumnSyncJob() {
		return jobBuilderFactory.get("boardCountColumnSyncJob")
				.start(boardCountColumnSyncStep())
				.build();
	}

	@Bean
	public Step boardCountColumnSyncStep() {
		return stepBuilderFactory.get("boardCountColumnSyncStep")
				.transactionManager(transactionManager)
				.<BoardCountColumn, BoardCountColumn>chunk(10)
				.reader(boardCountColumnReader())
				.writer(boardCountColumnWriter())
				.faultTolerant()
				.skipPolicy(new PermissiveSkipPolicy())
				.build();
	}
	
	@Bean
	public MyBatisPagingItemReader<BoardCountColumn> boardCountColumnReader() {
		MyBatisPagingItemReader reader = new MybatisRecentKeysetPagingItemReader<BoardCountColumn>();
		reader.setSqlSessionFactory(sqlSessionFactory);
		reader.setQueryId("com.codingjoa.mapper.BatchMapper.findBoardCountColumn");
		reader.setPageSize(10);
		reader.setMaxItemCount(50);
		return reader;
	}

	@Bean
	public ItemWriter<BoardCountColumn> boardCountColumnWriter() {
		MyBatisBatchItemWriter writer = new MyBatisBatchItemWriter<BoardCountColumn>() {
			@Override
			public void write(List<? extends BoardCountColumn> items) {
				log.info("## MyBatisBatchItemWriter.write");
				items.stream().forEach(boardCountColumn -> log.info("\t > {}", boardCountColumn.getMismatchDetails()));
				super.write(items);
			}
		};
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.syncBoardCountColumn");
		return writer;
	}
	
	/******************************************************************************************/
	/******************************************************************************************/
	
	@Bean
	public Job commentCountColumnSyncJob() {
		return jobBuilderFactory.get("commentCountColumnSyncJob")
				.start(commentCountColumnSyncStep())
				.build();
	}
	
	@Bean
	public Step commentCountColumnSyncStep() {
		return stepBuilderFactory.get("commentCountColumnSyncStep")
				.transactionManager(transactionManager)
				.<CommentCountColumn, CommentCountColumn>chunk(10)
				.reader(commentCountColumnReader())
				.writer(commentCountColumnWriter())
				.faultTolerant()
				.skipPolicy(new PermissiveSkipPolicy())
				.build();
	}
	
	@Bean
	public MyBatisPagingItemReader<CommentCountColumn> commentCountColumnReader() {
		MyBatisPagingItemReader reader = new MyBatisPagingItemReader<CommentCountColumn>();
		reader.setSqlSessionFactory(sqlSessionFactory);
		reader.setQueryId("com.codingjoa.mapper.BatchMapper.findCommentCountColumn");
		reader.setPageSize(10);
		reader.setMaxItemCount(50);
		return reader;
	}
	
	@Bean
	public ItemWriter<CommentCountColumn> commentCountColumnWriter() {
		MyBatisBatchItemWriter writer = new MyBatisBatchItemWriter<CommentCountColumn>() {
			@Override
			public void write(List<? extends CommentCountColumn> items) {
				log.info("## MyBatisBatchItemWriter.write");
				items.stream().forEach(commentCountColumn -> log.info("\t > {}", commentCountColumn.getMismatchDetails()));
				super.write(items);
			}
		};
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.syncCommentCountColumn");
		return writer;
	}
	
	
}
