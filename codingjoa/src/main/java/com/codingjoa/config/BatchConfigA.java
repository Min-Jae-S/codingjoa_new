package com.codingjoa.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EnableBatchProcessing // automatically registers some of its key components, such as JobBuilderFactory and StepBuilderFactory, as beans
@Configuration
public class BatchConfigA {
	
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
	
	@Bean
	public JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
	    factory.setDataSource(dataSource);
	    factory.setTransactionManager(transactionManager);
	    factory.setDatabaseType("ORACLE");
	    factory.setTablePrefix("BATCH_");
	    
	    // ORA-08177: can't serialize access for this transaction
	    // The issue arises when multiple Spring Batch jobs share a single JobRepository and are executed concurrently. 
	    // This occurs because, by default, the IsolationLevelForCreate property of the transactionManager is set to ISOLATION_SERIALIZABLE.
	    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT"); // ISOLATION_READ_COMMITTED
	    factory.afterPropertiesSet();
	    return factory.getObject();
	}
	
	public void printBatchConfig() throws Exception {
		String dataSourceUrl = dataSource.getConnection().getMetaData().getURL();
		log.info("\t > batch dataSource = {}", dataSource);
        log.info("\t > batch dataSource URL = {}", dataSourceUrl);
        log.info("\t > batch transaction manager = {}", transactionManager);
	}
}
