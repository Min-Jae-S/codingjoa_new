package com.codingjoa.service.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestJdbcService {
	
	public void jdbcBasic() throws ClassNotFoundException, SQLException {
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		String user = "codingjoa";
		String password = "1234";
		Class.forName("oracle.jdbc.driver.OracleDriver");

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		conn = DriverManager.getConnection(url, user, password);
		stmt = conn.createStatement();
		String query = "SELECT * FROM test3 ORDER BY idx DESC";
		rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			int idx = rs.getInt("idx");
			int num = rs.getInt("num");
			log.info("\t > idx = {}, num = {}", idx, num);
		}
		
		rs.close();
		stmt.close();
		conn.close();
	}
	
	public void jdbcTemplate() {
		
	}
}
	
	
