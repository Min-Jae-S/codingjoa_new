package com.codingjoa.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
//@EnableBatchProcessing
@Configuration
public class BatchConfig2 extends DefaultBatchConfigurer {
	
	private DataSource dataSource;

	@Autowired
	@Qualifier("batchTransactionManager")
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	@Override
	public void setDataSource(@Qualifier("batchDataSource") DataSource dataSource) {
		log.info("## BatchConfig2 setDataSource");
		log.info("\t > dataSource = {}", dataSource);
		super.setDataSource(dataSource);
	}

	@Override
	public PlatformTransactionManager getTransactionManager() {
		log.info("## BatchConfig2 getTransactionManager");
		return this.transactionManager;
	}
	
	
}
