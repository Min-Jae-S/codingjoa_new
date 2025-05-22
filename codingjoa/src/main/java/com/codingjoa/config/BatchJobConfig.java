package com.codingjoa.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
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
import com.codingjoa.entity.UserImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"rawtypes", "unchecked", "resource"})
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
	
	/******************************************************************************************/
	
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
	
	@StepScope
	@Bean
	public Tasklet userImageDummyTaskelet(@Value("#{jobParameters['userImageDir']}") String userImageDir) {
		List<Long> userIds = List.of(1L, 6021L, 6041L);
		return (contribution, chunkContext) -> {
			log.info("## UserImageDummyTaskelet");
			SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
			sqlSessionTemplate.update("com.codingjoa.mapper.BatchMapper.resetUserImageAllLatestFlag");
				
			File folder = new File(userImageDir);
			if (!folder.exists()) {
				folder.mkdirs();
			}
				
			Resource resource = new ClassPathResource("static/dummy_base.jpg");
			final int MAX = 10;
			
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
				log.info("## BoardImageItemWriter");
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
				log.info("## UserImageItemWriter");
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
	
	@Bean
	public SkippedIdCatchListener skippedIdCatchListener() {
		return new SkippedIdCatchListener();
	}
	
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
				log.info("## BoardCountColumnWriter");
				items.stream().forEach(boardCountColumn -> log.info("\t > {}", boardCountColumn.getMismatchDetails()));
				super.write(items);
			}
		};
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.syncBoardCountColumn");
		return writer;
	}
	
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
				log.info("## CommentCountColumnWriter");
				items.stream().forEach(commentCountColumn -> log.info("\t > {}", commentCountColumn.getMismatchDetails()));
				super.write(items);
			}
		};
		writer.setSqlSessionFactory(sqlSessionFactory);
		writer.setStatementId("com.codingjoa.mapper.BatchMapper.syncCommentCountColumn");
		return writer;
	}
	
	
}
