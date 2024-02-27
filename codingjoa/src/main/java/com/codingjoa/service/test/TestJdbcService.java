package com.codingjoa.service.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.codingjoa.test.TestItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestJdbcService {
	
	// JDBC driver, database URL, database credentials
	public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER = "codingjoa";
	public static final String PASSWORD = "1234";
	
	@Autowired
	@Qualifier("mainDataSource")
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate template;
	
	public void jdbcBasic() {
		log.info("## jdbcBasic - service");
		Connection conn = null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;

		try {
			// register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

			// prepare the statement
			String sql = "SELECT * FROM test3";
			pstmt = conn.prepareStatement(sql);
			
			// execute a query
			rs = pstmt.executeQuery();
			List<TestItem> list = new ArrayList<>();
			while (rs.next()) {
				int idx = rs.getInt("idx");
				int num = rs.getInt("num");
				list.add(new TestItem(idx, num));
			}
			log.info("\t > {}", list);
		} catch (ClassNotFoundException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} catch (SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			close(rs, pstmt, conn);
		}
	}
	
	public void jdbcDataSource() {
		log.info("## jdbcDataSource - service");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// open a connection
			conn = dataSource.getConnection();
			
			// prepare the statement
			String sql = "SELECT * FROM test3";
			pstmt = conn.prepareStatement(sql);
			
			// execute a query
			rs = pstmt.executeQuery();
			List<TestItem> list = new ArrayList<>();
			while (rs.next()) {
				int idx = rs.getInt("idx");
				int num = rs.getInt("num");
				list.add(new TestItem(idx, num));
			}
			log.info("\t > {}", list);
		} catch (SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			close(rs, pstmt, conn);
		}
	}
	
	public void jdbcTemplate() {
		log.info("## jdbcTemplate - service");
		
		String sql = "SELECT * FROM test3";
		List<TestItem> list1 = template.query(sql, new RowMapper<TestItem>() {
			@Override
			public TestItem mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TestItem(rs.getInt("idx"), rs.getInt("num"));
			}
		});
		
		List<TestItem> list2 = template.query(sql, (rs, rowNum) -> {
			return new TestItem(rs.getInt("idx"), rs.getInt("num"));
		});
		
		log.info("\t > list1 = {}", list1);
		log.info("\t > list2 = {}", list2);
	}
	
	private void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.info("\t > rs error, {}", e.getClass().getSimpleName());
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.info("\t > pstmt error, {}", e.getClass().getSimpleName());
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.info("\t > conn error, {}", e.getClass().getSimpleName());
			}
		}
	}
}
	
	
