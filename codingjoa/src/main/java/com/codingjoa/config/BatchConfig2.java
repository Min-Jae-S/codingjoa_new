package com.codingjoa.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@Configuration
public class BatchConfig2 /* extends DefaultBatchConfigurer */ {
	
//	@Bean
//	public BatchConfigurer batchConfigurer(@Qualifier("batchDataSource") DataSource dataSource) {
//		log.info("## BatchConfig2");
//		log.info("\t > batchDataSource = {}", dataSource);
//		DefaultBatchConfigurer batchConfigurer = new DefaultBatchConfigurer(dataSource);
//		log.info("\t > batchConfigurer= {}", batchConfigurer);
//		return batchConfigurer;
//	}
	
//	@Autowired
//	@Qualifier("batchTransactionManager")
//	private PlatformTransactionManager transactionManager;
//	
//	@Autowired
//	@Override
//	public void setDataSource(@Qualifier("batchDataSource") DataSource dataSource) {
//		log.info("## setDataSource");
//		log.info("\t > dataSource = {}", dataSource);
//		super.setDataSource(dataSource);
//	}
//
//	@Override
//	public PlatformTransactionManager getTransactionManager() {
//		log.info("## getTransactionManager");
//		return this.transactionManager;
//	}
	
}
