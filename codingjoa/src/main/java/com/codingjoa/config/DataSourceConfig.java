package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
@MapperScan("com.codingjoa.mapper")
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
	public HikariConfig mainHikariConfig() {
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
	
	@Primary // for @Transactional
	@Bean(name = "mainTransactionManager")
	public PlatformTransactionManager mainTransactionManager() {
		return new DataSourceTransactionManager(mainDataSource());
	}
	
	@Bean(name = "subTransactionManager")
	public PlatformTransactionManager subTransactionManager() {
		return new DataSourceTransactionManager(mainDataSource());
	}

	@Bean(name = "batchTransactionManager")
	public PlatformTransactionManager batchTransactionManager() {
		return new DataSourceTransactionManager(batchDataSource());
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(mainDataSource());
		factoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/com/codingjoa/mapper/**.xml"));
		return factoryBean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		log.info("## sqlSessionTemplate");
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
		org.apache.ibatis.session.Configuration config = template.getConfiguration();
		log.info("\t > mappers");
		for (Class<?> mappers : config.getMapperRegistry().getMappers()) {
			log.info("\t    - {}", mappers);
		}
		log.info("\t > details");
		log.info("\t    - jdbcTypeForNull = {}", config.getJdbcTypeForNull());
		log.info("\t    - mapUnderscoreToCamelCase = {}", config.isMapUnderscoreToCamelCase());
		log.info("\t    - callSettersOnNulls = {}", config.isCallSettersOnNulls());
		log.info("\t    - returnInstanceForEmptyRow = {}", config.isReturnInstanceForEmptyRow());
		return template;
	}
	
}
