package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@Configuration
public class BatchConfigB extends DefaultBatchConfigurer {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ BatchConfigB");
		log.info("===============================================================");
	}
	
	@Override
	protected JobRepository createJobRepository() throws Exception {
		log.info("## createJobRepository");
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
	    factory.setDataSource(dataSource);
	    factory.setTransactionManager(transactionManager);
	    factory.setDatabaseType("ORACLE");
	    factory.setTablePrefix("BATCH_");
	    
	    // ORA-08177: can't serialize access for this transaction
	    // The issue arises when multiple Spring Batch jobs share a single JobRepository and are executed concurrently. 
	    // This occurs because, by default, the IsolationLevelForCreate property of the transactionManager is set to ISOLATION_SERIALIZABLE 
	    // when not specified separately.
	    factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
	    factory.afterPropertiesSet();
	    return factory.getObject();
	}
	
	@Override
	public PlatformTransactionManager getTransactionManager() {
		log.info("## getTransactionManager");
		log.info("\t > this.transactionManager is null ? {}", transactionManager == null);
		return this.transactionManager;
	}

	public void printBatchConfig() throws Exception {
		String dataSourceUrl = dataSource.getConnection().getMetaData().getURL();
		log.info("\t > batch dataSource = {}", dataSource);
        log.info("\t > batch dataSource URL = {}", dataSourceUrl);
        log.info("\t > batch transaction manager = {}", getTransactionManager().getClass());
	}
	
}
