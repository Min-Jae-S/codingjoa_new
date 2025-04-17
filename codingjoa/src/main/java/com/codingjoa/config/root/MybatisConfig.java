package com.codingjoa.config.root;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.codingjoa.repository") // TEST
@MapperScan("com.codingjoa.mapper")
@Configuration
public class MybatisConfig {
	
	/*
	 * @@ SqlSessionFactoryBean
	 * 	- a Java component (Bean) responsible for creating the SqlSessionFactory
	 * 	- SqlSessionFactory: controls the execution with the global information of MyBatis and creates SqlSession
	 * 	- SqlSession: executes queries (created per operation unit by the factory)
	 */
	
	@Bean 
	public SqlSessionFactory sqlSessionFactory(@Qualifier("mainDataSource") DataSource dataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/com/codingjoa/mapper/**.xml"));
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}
	
	/*
	 * @@ When integrating MyBatis with Spring for enhanced productivity, the SqlSessionTemplate class is utilized.
	 * 	- SqlSessionFactory is injected through the constructor
	 * 	- SqlSessionTemplate supports declarative transaction management(@Transactional = AOP-based Transaction Control)
	 * 	- manages the lifecycle of a session, including closing the session and handling commit or rollback operations. 
	 * 	  Additionally, it handles the conversion of MyBatis exceptions to DataAccessException
	 * 
	 * @@ https://barunmo.blogspot.com/2013/06/mybatis.html
	 * 	The SqlSessionTemplate object, in contrast to the SqlSession object, 
	 * 	automatically manages resource release concerns by internally invoking the close() method through interceptors. 
	 * 	When developers use the SqlSessionTemplate object, they no longer need to be concerned about resource release problems in MyBatis. 
	 * 	However, MyBatis-Spring introduces a different challenge related to database transactions. 
	 * 	With SqlSessionTemplate, developers cannot use the commit() and rollback() methods programmatically. 
	 * 	In other words, the use of SqlSessionTemplate restricts the programmatic management of transactions.
	 * 
	 */
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}
