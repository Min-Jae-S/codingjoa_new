package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@Configuration
public class BatchConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ BatchConfig init");
		log.info("===============================================================");
	}
	
	@Bean
	public JobRepository jobRepository() throws Exception {
		log.info("## JobRepository");
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
	    factory.setDataSource(dataSource);
	    factory.setTransactionManager(transactionManager);
	    factory.setDatabaseType("ORACLE");
	    factory.setTablePrefix("BATCH_");
	    
	    // ORA-08177: can't serialize access for this transaction
	    // The issue arises when multiple Spring Batch jobs share a single JobRepository and are executed concurrently. 
	    // This occurs because, by default, the IsolationLevelForCreate property of the transactionManager is set to ISOLATION_SERIALIZABLE.
	    factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED"); // ISOLATION_DEFAULT
	    factory.afterPropertiesSet();
	    JobRepository jobRepository = factory.getObject();
	    log.info("\t > jobRepository = {}", jobRepository);
	    return jobRepository;
	}
	
}
