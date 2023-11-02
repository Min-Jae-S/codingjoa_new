package com.codingjoa.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MybatisConfig {
	
	@Autowired
	@Qualifier("mainDataSource")
	private DataSource dataSource;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ MybatisConfig");
		log.info("\t > dataSource = {}", this.dataSource);
		log.info("===============================================================");
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
		factory.setMapperLocations(applicationContext.getResources("classpath:/com/codingjoa/mapper/**.xml"));
		return factory.getObject();
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
