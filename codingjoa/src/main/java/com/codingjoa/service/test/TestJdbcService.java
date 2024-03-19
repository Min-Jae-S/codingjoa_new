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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.test.TestItem;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Service
public class TestJdbcService {
	
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
	
	public TestJdbcService(@Qualifier("mainDataSource") DataSource dataSource, 
			@Qualifier("mainTransactionManager") PlatformTransactionManager txManager) {
		this.dataSource = dataSource;
		this.txManager = txManager;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		log.info("## close resources (conn, pstmt, rs)");
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
	
	private void checkTransactionBySyncManager() {
		log.info("----------------------------------------------------------------------------------------------");
		log.info("## checkTransactionBySyncManager");
		log.info("\t > current transaction = {}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("\t > active transaction = {}", TransactionSynchronizationManager.isActualTransactionActive());
		
		boolean syncActive = TransactionSynchronizationManager.isSynchronizationActive();
		log.info("\t > sync active = {}", syncActive);
		log.info("\t > syncs = {}", syncActive == true ? TransactionSynchronizationManager.getSynchronizations() : "no sync");
		log.info("----------------------------------------------------------------------------------------------");
	}
	
	public void useDriverManager() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			// register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// open a connection from driver manager
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			conn.setAutoCommit(false);
			
			// @@ insert
			// prepare the statement
			pstmt = conn.prepareStatement(INSERT_SQL);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			// execute a query
			pstmt.executeUpdate();
			
			/*
			 * @@ https://stackoverflow.com/questions/32736040/after-an-exception-closing-the-connection-appears-to-commit-the-transaction-eve
			 * setting auto-commit to false means that a statement's changes won't be committed right after it's executed.
			 * It does not [necessarily] affect, however, the behavior of close(),
			 * which may choose to either commit or rollback uncommitted data. As the documentation states:
			 * 	 
			 * 	| It is strongly recommended that an application explicitly commits or rolls back an active transaction 
			 * 	| prior to calling the close method. If the close method is called and there is an active transaction, 
			 * 	| the results are implementation-defined.
			 * 
			 * In other words, regardless of the auto commit flag, 
			 * you should always explicitly commit() or rollback() a Connection object before close()ing it
			 * 
			 */
			
			conn.rollback();
		} catch (ClassNotFoundException | SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			close(conn, pstmt);
		}
	}
	
	public void useDataSource() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// open a connection from autowired data source
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			log.info("\t > auto commit = {}", conn.getAutoCommit());
			
			// @@ insert
			// prepare the statement
			pstmt = conn.prepareStatement(INSERT_SQL);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			// execute a query
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			close(conn, pstmt, rs);
		}
	}
	
	public List<TestItem> findTestItems() {
		return jdbcTemplate.query(SELECT_SQL, (rs, rowNum) -> {
			int idx = rs.getInt("idx");
			int num = rs.getInt("num");
			log.info("\t > idx = {},\tnum = {}", idx, num);
			return new TestItem(idx, num);
		});
	}
	
	public void deleteTestItems() {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		jdbcTemplate.update(DELETE_SQL);
		txManager.commit(status);
	}
	
	public void useJdbcTemplate() {
		jdbcTemplate.update(INSERT_SQL, RandomUtils.nextInt(1, 999));

		// anonymous class
		List<TestItem> list1 = jdbcTemplate.query(SELECT_SQL, new RowMapper<TestItem>() {
			@Override
			public TestItem mapRow(ResultSet rs, int rowNum) throws SQLException {
				int idx = rs.getInt("idx");
				int num = rs.getInt("num");
				//log.info("\t > idx = {},\tnum = {}", idx, num);
				return new TestItem(idx, num);
			}
		});
		
		// lamda
		List<TestItem> list2 = jdbcTemplate.query(SELECT_SQL, (rs, rowNum) -> {
			return new TestItem(rs.getInt("idx"), rs.getInt("num"));
		});
	}
	
	public void useProgrammaticTx(boolean commit) {
		log.info("\t > will {}", (commit == true) ? "commit" : "rollback");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("useProgrammaticTx");
		TransactionStatus status = txManager.getTransaction(def);
		checkTransaction(status);
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(INSERT_SQL);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			pstmt.executeUpdate();
			if (commit) {
				txManager.commit(status);
			} else {
				txManager.rollback(status);
			}
		} catch (SQLException e) {
			txManager.rollback(status);
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			close(conn, pstmt);
		}
	}

	public void useDeclarativeTx() {
		log.info("## useDeclarativeTx - service");
	}
	
}
