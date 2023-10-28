package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
	public HikariConfig oracleHikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("datasource.oracle.classname"));
		hikariConfig.setJdbcUrl(env.getProperty("datasource.oracle.url"));
		hikariConfig.setUsername(env.getProperty("datasource.oracle.username"));
		hikariConfig.setPassword(env.getProperty("datasource.oracle.password"));
		return hikariConfig;
	}
	
	@Bean
	public HikariConfig h2HikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("datasource.h2.classname"));
		hikariConfig.setJdbcUrl(env.getProperty("datasource.h2.url"));
		hikariConfig.setUsername(env.getProperty("datasource.h2.username"));
		hikariConfig.setPassword(env.getProperty("datasource.h2.password"));
		return hikariConfig;
	}
	
	@Bean
	public DataSource oracleDataSource() {
		return new HikariDataSource(oracleHikariConfig());
	}

	//@Primary
	@Bean
	public DataSource h2DataSource() {
		return new HikariDataSource(h2HikariConfig());
	}
	
	//@Primary
	@Bean
	public PlatformTransactionManager oracleTransactionManager() {
		return new DataSourceTransactionManager(oracleDataSource());
	}

	@Bean
	public PlatformTransactionManager h2TransactionManager() {
		return new DataSourceTransactionManager(h2DataSource());
	}

	public void printDataSoruceConfig() throws Exception {
		log.info("\t > main dataSource = {}", oracleDataSource());
        log.info("\t > main dataSource URL = {}", oracleDataSource().getConnection().getMetaData().getURL());
        log.info("\t > main transaction manager = {}", oracleTransactionManager());
        log.info("\t > main transaction manager is proxy ? {}", AopUtils.isAopProxy(oracleTransactionManager()));
	}
	
}
