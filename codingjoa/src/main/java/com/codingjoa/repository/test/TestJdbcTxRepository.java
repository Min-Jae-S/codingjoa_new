package com.codingjoa.repository.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Repository
public class TestJdbcTxRepository {

	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;
	
	public TestJdbcTxRepository(@Qualifier("mainDataSource") DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
}
