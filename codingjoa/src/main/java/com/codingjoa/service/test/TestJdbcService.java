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
	
	private final DataSource dataSource;
	private final JdbcTemplate template;
	private final PlatformTransactionManager transactionManager;
	
	public TestJdbcService(@Qualifier("mainDataSource") DataSource dataSource, 
			PlatformTransactionManager transactionManager) {
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
		this.template = new JdbcTemplate(dataSource);
	}
	
	private void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		log.info("## close conn, pstmt, rs");
		try {
			if (rs != null) 	rs.close();
			if (pstmt != null) 	pstmt.close();
			if (conn != null)	conn.close();
		} catch (SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		}
	}
	
	public void basicJdbc() {
		log.info("## basicJdbc - service");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// open a connection from driver manager
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			// @@ insert
			// prepare the statement
			pstmt = conn.prepareStatement(INSERT_SQL);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			// execute a query
			int rows = pstmt.executeUpdate();
			if (rows > 0) {
				log.info("\t > insert success");
			}
			
			/*
			 * @@ https://stackoverflow.com/questions/32736040/after-an-exception-closing-the-connection-appears-to-commit-the-transaction-eve
			 * setting auto-commit to false means that a statement's changes won't be committed right after it's executed.
			 * It does not [necessarily] affect, however, the behavior of close(),
			 * which may choose to either commit or rollback uncommitted data. As the documentation states:
			 * 	 
			 * | It is strongly recommended that an application explicitly commits or rolls back an active transaction 
			 * | prior to calling the close method. If the close method is called and there is an active transaction, 
			 * | the results are implementation-defined.
			 * 
			 * In other words, regardless of the auto commit flag, 
			 * you should always explicitly commit() or rollback() a Connection object before close()ing it
			 */
			
		} catch (Exception e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			close(rs, pstmt, conn);
		}
	}
	
	public void basicJdbc2() {
		log.info("## basicJdbc2 - service");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// open a connection from autowired data source
			conn = dataSource.getConnection();
			log.info("\t > auto commit = {}", conn.getAutoCommit());
			
			// @@ insert
			// prepare the statement
			pstmt = conn.prepareStatement(INSERT_SQL);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			// execute a query
			int rows = pstmt.executeUpdate();
			if (rows > 0) {
				log.info("\t > insert success");
			}
		} catch (Exception e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			close(rs, pstmt, conn);
		}
	}
	
	public List<TestItem> findTestItems() {
		log.info("## findTestItems - service");
		return template.query(SELECT_SQL, (rs, rowNum) -> {
			int idx = rs.getInt("idx");
			int num = rs.getInt("num");
			log.info("\t > idx = {},\tnum = {}", idx, num);
			return new TestItem(idx, num);
		});
	}
	
	public void springJdbc() {
		log.info("## springJdbc - service");
		template.update(INSERT_SQL, RandomUtils.nextInt(1, 999));

		// anonymous class
		List<TestItem> list1 = template.query(SELECT_SQL, new RowMapper<TestItem>() {
			@Override
			public TestItem mapRow(ResultSet rs, int rowNum) throws SQLException {
				int idx = rs.getInt("idx");
				int num = rs.getInt("num");
				//log.info("\t > idx = {},\tnum = {}", idx, num);
				return new TestItem(idx, num);
			}
		});
		
		// lamda
		List<TestItem> list2 = template.query(SELECT_SQL, (rs, rowNum) -> {
			return new TestItem(rs.getInt("idx"), rs.getInt("num"));
		});
	}
	
}
