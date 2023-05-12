package com.codingjoa.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.annotation.SessionScope;

import com.codingjoa.dto.SessionDto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("/WEB-INF/properties/db.properties")
@MapperScan("com.codingjoa.mapper")
@ComponentScan(basePackages = { "com.codingjoa.service", "com.codingjoa.response" })
public class RootConfig {

	@Value("${db.classname}")
	private String classname;

	@Value("${db.url}")
	private String url;

	@Value("${db.username}")
	private String username;

	@Value("${db.password}")
	private String password;
	
	@Bean
	public HikariConfig hikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(classname);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);

		return hikariConfig;
	}

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}

	@Bean
	public SqlSessionFactory factory(ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/com/codingjoa/mapper/**.xml"));

		return factoryBean.getObject();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		return modelMapper;
	}
	
	@Bean
	@SessionScope
	public SessionDto sessionDto() {
		return new SessionDto();
	}
}
