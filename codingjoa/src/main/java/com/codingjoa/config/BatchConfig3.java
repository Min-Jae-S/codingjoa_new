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
@EnableBatchProcessing
@Configuration
public class BatchConfig3 extends DefaultBatchConfigurer {
	
	@Autowired
	@Qualifier("batchTransactionManager")
	private PlatformTransactionManager transactionManager;
	
	@Autowired(required = false)
	@Override
	public void setDataSource(@Qualifier("batchDataSource") DataSource dataSource) {
		log.info("## BatchConfig3 setDataSource");
		log.info("\t > dataSource = {}", dataSource);
		super.setDataSource(dataSource);
	}

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return this.transactionManager;
	}
	
	
	
	
}
