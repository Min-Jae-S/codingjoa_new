package com.codingjoa.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.annotation.SessionScope;

import com.codingjoa.dto.SessionDto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("/WEB-INF/properties/db.properties")
@MapperScan("com.codingjoa.mapper")
public class RootConfig {
	
	@Value("${db.classname}")
	private String driverClassName;

	@Value("${db.url}")
	private String url;
	
	@Value("${db.username}")
	private String username;
	
	@Value("${db.password}")
	private String password;
	
	@Bean
	public HikariConfig hikariConfig() {
		log.info("## DBCP, HikariCP Bean");
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driverClassName);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		log.info("\t > auto commit = {}", hikariConfig.isAutoCommit());
		
		return hikariConfig;
	}

	@Bean
	public DataSource dataSource() {
		log.info("## DataSoruce Bean");
		DataSource dataSource = new HikariDataSource(hikariConfig());
		log.info("datasource connection = {}", dataSource);
		
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		log.info("## TransactionManager Bean");
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		
		return transactionManager;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath:/com/codingjoa/mapper/**.xml"));
		return sessionFactory.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		log.info("## SqlSessionTemplate Bean");
		SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		org.apache.ibatis.session.Configuration config = sessionTemplate.getConfiguration();
		log.info("\t > jdbcTypeForNull = {}", config.getJdbcTypeForNull());
		log.info("\t > mapUnderscoreToCamelCase = {}", config.isMapUnderscoreToCamelCase());
		log.info("\t > callSettersOnNulls = {}", config.isCallSettersOnNulls());
		log.info("\t > returnInstanceForEmptyRow = {}", config.isReturnInstanceForEmptyRow());
		
		return sessionTemplate;
	}
	
	@Bean
	public ModelMapper modelMapper() {
		log.info("## ModelMapper Bean");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
			.setMatchingStrategy(MatchingStrategies.STRICT)
			// setter 없는 Dto를 mapping 하기 위해 fieldAccessLevel과 fieldMatchingEnabled를 설정 
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
		
		org.modelmapper.config.Configuration config = modelMapper.getConfiguration();
		log.info("\t > matchingStrategy = {}", config.getMatchingStrategy());
		log.info("\t > fieldAccessLevel = {}", config.getFieldAccessLevel());
		log.info("\t > methodAccessLevel = {}", config.getMethodAccessLevel());
		log.info("\t > fieldMatchingEnabled = {}", config.isFieldMatchingEnabled());
		log.info("\t > collectionsMergeEnabled = {}", config.isCollectionsMergeEnabled());
		
		return modelMapper;
	}
	
	@Bean @SessionScope
	public SessionDto sessionDto() {
		return new SessionDto();
	}
}
