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
@EnableBatchProcessing //automatically registers some of its key components, such as JobBuilderFactory and StepBuilderFactory, as beans
@Configuration
public class BatchConfig {
	
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
		log.info("\t > dataSource = {}", this.dataSource);
		log.info("\t > transactionManager = {}", this.transactionManager);
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
	    // This occurs because, by default, the IsolationLevelForCreate property of the transactionManager is set to ISOLATION_SERIALIZABLE.
	    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT"); // ISOLATION_READ_COMMITTED
	    factory.afterPropertiesSet();
	    return factory.getObject();
	}
}
