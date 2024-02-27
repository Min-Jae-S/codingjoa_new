package com.codingjoa.service.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestJdbcService {
	
	// JDBC driver and database URL
	final static String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final static String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	
	// database credentials
	final static String USER = "codingjoa";
	final static String PASSWORD = "1234";
	
	@Autowired
	@Qualifier("mainDataSource")
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void jdbcBasic() throws ClassNotFoundException, SQLException {
		log.info("## service - jdbcBasic");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// register JDBC driver
		Class.forName(JDBC_DRIVER);
		
		// open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
		
		// prepare the statement
		String sql = "SELECT * FROM test3 ORDER BY idx DESC";
		pstmt = conn.prepareStatement(sql);
		
		// execute a query
		rs = pstmt.executeQuery();
		while (rs.next()) {
			int idx = rs.getInt("idx");
			int num = rs.getInt("num");
			log.info("\t > idx = {}, num = {}", idx, num);
		}
		
		rs.close();
		pstmt.close();
		conn.close();
	}
	
	public void jdbcDataSource() throws SQLException {
		log.info("## service - jdbcDataSource");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// open a connection
		conn = dataSource.getConnection();
		
		// prepare the statement
		String sql = "SELECT * FROM test3 ORDER BY idx DESC";
		pstmt = conn.prepareStatement(sql);
		
		// execute a query
		rs = pstmt.executeQuery();
		while (rs.next()) {
			int idx = rs.getInt("idx");
			int num = rs.getInt("num");
			log.info("\t > idx = {}, num = {}", idx, num);
		}
		
		rs.close();
		pstmt.close();
		conn.close();
	}
	
	public void jdbcTemplate() {
		log.info("## service - jdbcTemplate");
	}
}
	
	
