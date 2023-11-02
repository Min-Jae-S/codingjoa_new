package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("/WEB-INF/properties/datasource.properties")
public class DataSourceConfig {
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ DataSourceConfig");
		log.info("===============================================================");
	}
	
	@Bean
	public HikariConfig hikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("datasource.main.classname"));
		hikariConfig.setJdbcUrl(env.getProperty("datasource.main.url"));
		hikariConfig.setUsername(env.getProperty("datasource.main.username"));
		hikariConfig.setPassword(env.getProperty("datasource.main.password"));
		hikariConfig.setPoolName("MainHikariPool");
		return hikariConfig;
	}
	
	@Bean
	public HikariConfig batchHikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("datasource.batch.classname"));
		hikariConfig.setJdbcUrl(env.getProperty("datasource.batch.url"));
		hikariConfig.setUsername(env.getProperty("datasource.batch.username"));
		hikariConfig.setPassword(env.getProperty("datasource.batch.password"));
		hikariConfig.setPoolName("BatchHikariPool");
		return hikariConfig;
	}
	
	//@Primary
	@Bean(name = "mainDataSource")
	public DataSource mainDataSource() {
		return new HikariDataSource(hikariConfig());
	}
	
	// Error creating bean with name 'batchJobConfig'
	// Error creating bean with name 'org.springframework.batch.core.configuration.annotation.SimpleBatchConfiguration'
	// NoUniqueBeanDefinitionException: No qualifying bean of type 'javax.sql.DataSource' available: 
	// expected single matching bean but found 2: oracleDataSource,h2DataSource
	@Bean(name = "batchDataSource")
	public DataSource batchDataSource() {
		return new HikariDataSource(batchHikariConfig());
	}
	
	//@Primary
	@Bean(name = "mainTransactionManager")
	public PlatformTransactionManager mainTransactionManager() {
		return new DataSourceTransactionManager(mainDataSource());
	}

	@Bean(name = "batchTransactionManager")
	public PlatformTransactionManager batchTransactionManager() {
		return new DataSourceTransactionManager(batchDataSource());
	}
	
}
