package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@Configuration
public class BatchConfig {
	
	/*
	 * Spring Batch - Application, Batch Core, Batch Infrastrcuture
	 * 	> Application 			: 개발자가 작성한 모든 배치 작업과 사용자 정의 코드 포함
	 * 	> Batch Core  			: 배치 작업을 시작하고 제어하는데 필요한 핵심 런타임 클래스 포함 (JobLauncher, Job, Step)
	 * 	> Batch Infrastructure	: 어플리케이션에서 사용하는 Tasklet, Reader, Processor, Writer 그리고 RetryTemplate과 같은 서비스를 포함
	 * 
	 * https://stackoverflow.com/questions/65607909/spring-batch-2-4-1-wildfly-20-final-java-lang-nosuchfielderror-block-unsafe
	 * Caused by: java.lang.NoSuchFieldError: BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES
	 * springbatch 4.3.0 has introduced a dependency on jackson databind 2.11.
	 * 
	 */
	
	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;
	
	public BatchConfig(@Qualifier("batchDataSource") DataSource dataSource, 
			@Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager) {
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
	}
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ BatchConfig");
		log.info("\t > batch dataSource = {}", dataSource);
		log.info("\t > batch transactionManager = {}", transactionManager);
		log.info("===============================================================");
	}
	
	@Bean
	public JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.setDatabaseType("H2");
	    factory.setTablePrefix("BATCH_");
	    
	    // ORA-08177: can't serialize access for this transaction
	    // The issue arises when multiple Spring Batch jobs share a single JobRepository and are executed concurrently. 
	    // This occurs because, by default, the IsolationLevelForCreate property of the transactionManager is set to ISOLATION_SERIALIZABLE 
	    // when not specified separately.
	    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
	    factory.afterPropertiesSet();
	    JobRepository jobRepository = factory.getObject();
	    log.info("## jobRepository" + System.lineSeparator() + "\t\t\t\t\t\t\t > {}", jobRepository);
		return jobRepository;
	}
	
}
