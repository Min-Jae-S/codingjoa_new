package com.codingjoa.service.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.repository.test.TestJdbcTxRepository;
import com.codingjoa.test.TestItem;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Service
public class TestJdbcTxService {
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	@Autowired
	private TestJdbcTxRepository jdbcRepository;

	private final DataSource dataSource;
	
	public TestJdbcTxService(@Qualifier("mainDataSource") DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void useTx(boolean commit) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			
			int num = RandomUtils.nextInt(1, 999);
			log.info("\t > num = {}", num);
			
			jdbcRepository.saveItem(conn, num);
			if (commit) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.info("\t > {}", e1.getClass().getSimpleName());
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				log.info("\t > {}", e.getClass().getSimpleName());
			}
		}
	}

	public void useTxSyncManager(boolean commit) {

	}
	
	public void useTxManager(boolean commit) {

	}
	
	public void usetDeclarativeTx(boolean commit) {

	}

}
