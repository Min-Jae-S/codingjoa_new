package com.codingjoa.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableTransactionManagement //(proxyTargetClass = true)
@Configuration
public class DataSourceConfig {
	
	private final Environment env;
	
	@Bean
	public HikariConfig mainHikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("datasource.main.classname"));
		hikariConfig.setJdbcUrl(env.getProperty("datasource.main.url"));
		hikariConfig.setUsername(env.getProperty("datasource.main.username"));
		hikariConfig.setPassword(env.getProperty("datasource.main.password"));
		hikariConfig.setPoolName("MainHikariPool");
		hikariConfig.setAutoCommit(false);
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
		hikariConfig.setAutoCommit(false);
		return hikariConfig;
	}
	
	@Bean(name = "mainDataSource")
	public DataSource mainDataSource() {
		return new HikariDataSource(mainHikariConfig());
	}
	
	// @EnableBatchProcessing: Error creating bean with name 'org.springframework.batch.core.configuration.annotation.SimpleBatchConfiguration'
	// NoUniqueBeanDefinitionException: No qualifying bean of type 'javax.sql.DataSource' available: 
	// expected single matching bean but found 2: mainDataSource,batchDataSource
	@Bean(name = "batchDataSource")
	public DataSource batchDataSource() {
		return new HikariDataSource(batchHikariConfig());
	}
	
	@Primary
	@Bean(name = "mainTransactionManager")
	public PlatformTransactionManager mainTransactionManager() {
		return new DataSourceTransactionManager(mainDataSource());
	}
	
	@Bean(name = "batchTransactionManager")
	public PlatformTransactionManager batchTransactionManager() {
		return new DataSourceTransactionManager(batchDataSource());
	}
	
}
