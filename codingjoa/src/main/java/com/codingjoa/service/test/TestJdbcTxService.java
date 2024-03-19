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
	
	private final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	private final String USER = "codingjoa";
	private final String PASSWORD = "1234";
	
	private final String SELECT_SQL = "SELECT * FROM test3 ORDER BY idx DESC";
	private final String INSERT_SQL = "INSERT INTO test3 (idx, num) VALUES (seq_test3.NEXTVAL, ?)";
	private final String DELETE_SQL = "DELETE FROM test3";
	
	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;
	private final PlatformTransactionManager txManager;
	
	@Autowired
	private TestJdbcTxRepository testJdbcTxRepository;
	
	public TestJdbcTxService(@Qualifier("mainDataSource") DataSource dataSource, 
			@Qualifier("mainTransactionManager") PlatformTransactionManager txManager) {
		this.dataSource = dataSource;
		this.txManager = txManager;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		log.info("## close resources (conn, pstmt, rs)");
		log.info("\t > conn = {}", conn);
		log.info("\t > pstmt = {}", pstmt);
		log.info("\t > rs = {}", rs);
		try {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		} catch (Exception e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		}
	}
	
	private void close(Connection conn, PreparedStatement pstmt) {
		log.info("## close resources (conn, pstmt)");
		log.info("\t > conn = {}", conn);
		log.info("\t > pstmt = {}", pstmt);
		try {
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		} catch (Exception e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		}
	}
	
	private void checkTransaction(TransactionStatus transactionStatus) {
		log.info("----------------------------------------------------------------------------------------------");
		log.info("## checkTransaction");
		if (transactionStatus == null) {
			log.info("\t > transaction status = NO transaction");
			return;
		}
		
		String status = null;
		if (transactionStatus.isCompleted()) {
			status = "completed";
		} else if (transactionStatus.isRollbackOnly()) {
			status = "rollback";
		} else if (transactionStatus.isNewTransaction()) {
			status = "new transaction";
		} else {
			status = "unknown";
		}
		log.info("\t > transaction status = {}", status);
		log.info("----------------------------------------------------------------------------------------------");
	}
	
	private void checkTransactionSync() {
		log.info("----------------------------------------------------------------------------------------------");
		log.info("## checkTransactionSync");
		log.info("\t > current transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("\t > active transaction = {}", TransactionSynchronizationManager.isActualTransactionActive());
		
		boolean syncActive = TransactionSynchronizationManager.isSynchronizationActive();
		log.info("\t > sync active = {}", syncActive);
		log.info("\t > syncs = {}", syncActive == true ? TransactionSynchronizationManager.getSynchronizations() : "no sync");
		log.info("----------------------------------------------------------------------------------------------");
	}
	
	
}
