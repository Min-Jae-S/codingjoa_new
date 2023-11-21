package com.codingjoa.config;

import javax.annotation.PostConstruct;
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
public class BatchConfig2 {
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ BatchConfig2");
		log.info("===============================================================");
	}
	
	@Bean
	public BatchConfigurer batchConfigurer(@Qualifier("batchDataSource") DataSource dataSource) {
		return new DefaultBatchConfigurer(dataSource) {
			@Override
			public void setDataSource(DataSource dataSource) {
				super.setDataSource(dataSource);
			}
		};
	}
	
}
