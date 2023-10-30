package com.codingjoa.config;

import javax.sql.DataSource;

import org.springframework.aop.support.AopUtils;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
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
public class BatchConfig extends DefaultBatchConfigurer {
	
	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;
	
	public BatchConfig(@Qualifier("batchDataSource") DataSource dataSource, 
			@Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager) {
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
	}
	
	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}
	
	@Override
	public PlatformTransactionManager getTransactionManager() {
		return this.transactionManager;
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
	
	public void printBatchConfig() throws Exception {
		String dataSourceUrl = dataSource.getConnection().getMetaData().getURL();
		log.info("\t > batch dataSource = {}", dataSource);
        log.info("\t > batch dataSource URL = {}", dataSourceUrl);
        log.info("\t > batch transaction manager = {}", transactionManager);
        log.info("\t > batch transaction manager is proxy ? {}", AopUtils.isAopProxy(transactionManager));
	}
}
