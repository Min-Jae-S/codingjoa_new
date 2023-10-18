package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("/WEB-INF/properties/datasource.properties")
@MapperScan("com.codingjoa.mapper")
public class RootConfig {
	
	@Value("${datasource.classname}")
	private String driverClassName;

	@Value("${datasource.url}")
	private String url;
	
	@Value("${datasource.username}")
	private String username;
	
	@Value("${datasource.password}")
	private String password;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ RootConfig");
		log.info("===============================================================");
	}
	
	@Bean
	public HikariConfig hikariConfig() {
		log.info("## HikariConfig (DBCP)");
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
		DataSource dataSource = new HikariDataSource(hikariConfig());
		log.info("## DataSoruce");
		log.info("\t > datasource = {}", dataSource);
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		log.info("## SqlSessionFactory");
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource());
		factory.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
		factory.setMapperLocations(applicationContext.getResources("classpath:/com/codingjoa/mapper/**.xml"));
		return factory.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		log.info("## SqlSessionTemplate");
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
		org.apache.ibatis.session.Configuration config = template.getConfiguration();
		log.info("\t > jdbcTypeForNull = {}", config.getJdbcTypeForNull());
		log.info("\t > mapUnderscoreToCamelCase = {}", config.isMapUnderscoreToCamelCase());
		log.info("\t > callSettersOnNulls = {}", config.isCallSettersOnNulls());
		log.info("\t > returnInstanceForEmptyRow = {}", config.isReturnInstanceForEmptyRow());
		return template;
	}
	
	@Bean
	public ModelMapper modelMapper() {
		log.info("## ModelMapper");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
			.setMatchingStrategy(MatchingStrategies.STRICT)
			// setter가 없는 Dto(UserDetailsDto)에 대한 mapping을 위해 fieldAccessLevel과 fieldMatchingEnabled를 설정 
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
		
		org.modelmapper.config.Configuration config = modelMapper.getConfiguration();
		log.info("\t > matchingStrategy = {}", config.getMatchingStrategy());
		log.info("\t > fieldAccessLevel = {}", config.getFieldAccessLevel());
		log.info("\t > methodAccessLevel = {}", config.getMethodAccessLevel());
		log.info("\t > propertyCondition = {}", config.getPropertyCondition());
		log.info("\t > isFieldMatchingEnabled = {}", config.isFieldMatchingEnabled());
		log.info("\t > isSkipNullEnabled = {}", config.isSkipNullEnabled());
		log.info("\t > isCollectionsMergeEnabled = {}", config.isCollectionsMergeEnabled());
		return modelMapper;
	}
	
	public void printRootConfig() throws Exception {
		String dataSourceUrl = dataSource().getConnection().getMetaData().getURL();
		log.info("\t > root  dataSource = {}", dataSource());
        log.info("\t > root  dataSource URL = {}", dataSourceUrl);
        log.info("\t > root  transaction manager = {}", transactionManager());
        log.info("\t > root  transaction manager is proxy ? {}", AopUtils.isAopProxy(transactionManager()));
	}
	
}
