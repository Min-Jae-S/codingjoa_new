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
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
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
			conn.setAutoCommit(false);
			log.info("\t > auto commit = {}", conn.getAutoCommit());
			
			// open a connection from autowired data source
			//conn = dataSource.getConnection();

			// @@ insert
			// prepare the statement
			pstmt = conn.prepareStatement(INSERT_SQL);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			// execute a query
			int rows = pstmt.executeUpdate();
			if (rows > 0) {
				log.info("\t > insert success");
			}
			
			log.info("-----------------------------------------");
			log.info("\t > sleeping !!");
			log.info("-----------------------------------------" + System.lineSeparator());
			TimeUnit.SECONDS.sleep(20);
			log.info("-----------------------------------------");
			log.info("\t > wake up !!");
			log.info("-----------------------------------------");
			
			// @@ select
			// prepare the statement
//			pstmt = conn.prepareStatement(SELECT_SQL);
//			
//			// execute a query
//			rs = pstmt.executeQuery();
//			List<TestItem> list = new ArrayList<>();
//			while (rs.next()) {
//				int idx = rs.getInt("idx");
//				int num = rs.getInt("num");
//				log.info("\t > idx = {},\tnum = {}", idx, num);
//				list.add(new TestItem(idx, num));
//			}
		} catch (ClassNotFoundException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} catch (SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} catch (InterruptedException e) {
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
			conn = dataSource.getConnection();
			log.info("\t > auto commit = {}", conn.getAutoCommit());
			
			pstmt = conn.prepareStatement(INSERT_SQL);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			int rows = pstmt.executeUpdate();
			if (rows > 0) {
				log.info("\t > insert success");
			}
		} catch (SQLException e) {
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

		// anonymous class
		List<TestItem> list1 = template.query(SELECT_SQL, new RowMapper<TestItem>() {
			@Override
			public TestItem mapRow(ResultSet rs, int rowNum) throws SQLException {
				int idx = rs.getInt("idx");
				int num = rs.getInt("num");
				log.info("\t > idx = {},\tnum = {}", idx, num);
				return new TestItem(idx, num);
			}
		});
		
		// lamda
		List<TestItem> list2 = template.query(SELECT_SQL, (rs, rowNum) -> {
			return new TestItem(rs.getInt("idx"), rs.getInt("num"));
		});
	}
	
}
