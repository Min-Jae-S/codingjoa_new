package com.codingjoa.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing // automatically registers some of its key components, such as JobBuilderFactory and StepBuilderFactory, as beans.
public class BatchConfig {
	
	// JobRepository, JobLauncher, JobRegistry,PlatformTransactionManager, 
	// JobBuilderFactory, StepBuilderFactory
	
	@Autowired
	private JobBuilderFactory JobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
}
