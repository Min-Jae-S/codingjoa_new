package com.codingjoa.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
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
@EnableBatchProcessing //automatically registers some of its key components, such as JobBuilderFactory and StepBuilderFactory, as beans
public class BatchConfig extends DefaultBatchConfigurer {
	
	// Spring Batch - Application, Batch Core, Batch Infrastrcuture
	// 	> Application 			: 개발자가 작성한 모든 배치 작업과 사용자 정의 코드 포함
	// 	> Batch Core  			: 배치 작업을 시작하고 제어하는데 필요한 핵심 런타임 클래스 포함 (JobLauncher, Job, Step)
	// 	> Batch Infrastructure	: 어플리케이션에서 사용하는 Tasklet, Reader, Processor, Writer 그리고 RetryTemplate과 같은 서비스를 포함

	// https://stackoverflow.com/questions/65607909/spring-batch-2-4-1-wildfly-20-final-java-lang-nosuchfielderror-block-unsafe
	// Caused by: java.lang.NoSuchFieldError: BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES
	// springbatch 4.3.0 has introduced a dependency on jackson databind 2.11.
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Override
	public void setDataSource(DataSource dataSource) {
		// do nothing..
	}

	// o.s.b.c.c.a.DefaultBatchConfigurer       : No datasource was provided...using a Map based JobRepository
	// o.s.b.c.c.a.DefaultBatchConfigurer       : No transaction manager was provided, using a ResourcelessTransactionManager
	// Map based JobRepository(un-memory repository)

	//	@PostConstruct
	//	public void initialize() {
	//		try {
	//			if(dataSource == null) {
	//				logger.warn("No datasource was provided...using a Map based JobRepository");
	//
	//				if(getTransactionManager() == null) {
	//					logger.warn("No transaction manager was provided, using a ResourcelessTransactionManager");
	//					this.transactionManager = new ResourcelessTransactionManager();
	//				}
	//
	//				MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(getTransactionManager());
	//				jobRepositoryFactory.afterPropertiesSet();
	//				this.jobRepository = jobRepositoryFactory.getObject();
	//
	//				MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(jobRepositoryFactory);
	//				jobExplorerFactory.afterPropertiesSet();
	//				this.jobExplorer = jobExplorerFactory.getObject();
	//			} else {
	//				this.jobRepository = createJobRepository();
	//				this.jobExplorer = createJobExplorer();
	//			}
	//
	//			this.jobLauncher = createJobLauncher();
	//		} catch (Exception e) {
	//			throw new BatchConfigurationException(e);
	//		}
	//	}
	
	@Bean
	public Job batchJob() {
		return jobBuilderFactory.get("batchJob")
				.start(step1())
				.next(step2())
				.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						log.info("\t ====== STEP1 ======");
						return RepeatStatus.FINISHED;
					}
				})
				.allowStartIfComplete(true) // Step already complete or not restartable, so no action to execute
				.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet((contribution, chunkContext) -> {
					log.info("\t ====== STEP2 ======");
					return RepeatStatus.FINISHED;
				})
				.allowStartIfComplete(true)
				.build();
	}
	
}
