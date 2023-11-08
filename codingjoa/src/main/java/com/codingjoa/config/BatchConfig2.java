package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BatchConfig2 extends DefaultBatchConfigurer {
	
	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;
	
	public BatchConfig2(@Qualifier("batchDataSource") DataSource dataSource, 
			@Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager) {
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
	}
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ BatchConfig2");
		log.info("\t > batch dataSource = {}", dataSource);
		log.info("\t > batch transactionManager = {}", transactionManager);
		log.info("===============================================================");
	}
	
	@Autowired
	@Override
	public void setDataSource(@Qualifier("batchDataSource") DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	protected JobRepository createJobRepository() throws Exception {
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
