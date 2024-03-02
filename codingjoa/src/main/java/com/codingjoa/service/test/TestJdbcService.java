package com.codingjoa.service.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.codingjoa.test.TestItem;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Service
public class TestJdbcService {
	
	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	static final String USER = "codingjoa";
	static final String PASSWORD = "1234";
	
	@Autowired
	@Qualifier("mainDataSource")
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate template;
	
	private void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		}
	}
	
	public void jdbcBasic() {
		log.info("## jdbcBasic - service");
		Connection conn = null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;

		String sql1 = "INSERT INTO test3 (idx, num) VALUES (seq_test3.NEXTVAL, ?)";
		String sql2 = "SELECT * FROM test3 ORDER BY idx DESC";
		try {
			// register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

			// prepare the statement
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			// execute a query
			int result = pstmt.executeUpdate();
			if (result > 0) {
				log.info("\t > SUCCESS");
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			
			pstmt = conn.prepareStatement(sql2);
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
		
		String sql1 = "INSERT INTO test3 (idx, num) VALUES (seq_test3.NEXTVAL, ?)";
		String sql2 = "SELECT * FROM test3 ORDER BY idx DESC";
		try {
			// open a connection
			conn = dataSource.getConnection();
			
			// prepare the statement
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, RandomUtils.nextInt(1, 999));
			
			// execute a query
			int result = pstmt.executeUpdate();
			if (result > 0) {
				log.info("\t > SUCCESS");
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			
			pstmt = conn.prepareStatement(sql2);
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
	
	public void springJdbc() {
		log.info("## springJdbc - service");
		
		String sql1 = "INSERT INTO test3 (idx, num) VALUES (seq_test3.NEXTVAL, ?)";
		String sql2 = "SELECT * FROM test3 ORDER BY idx DESC";
		
		// anonymous class
		List<TestItem> list1 = template.query(sql2, new RowMapper<TestItem>() {
			@Override
			public TestItem mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TestItem(rs.getInt("idx"), rs.getInt("num"));
			}
		});
		
		// lamda
		List<TestItem> list2 = template.query(sql2, (rs, rowNum) -> {
			return new TestItem(rs.getInt("idx"), rs.getInt("num"));
		});
		
		log.info("\t > list1 = {}", list1);
		log.info("\t > list2 = {}", list2);
	}
	
}
	
	
