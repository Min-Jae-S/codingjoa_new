package com.codingjoa.service.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codingjoa.test.TestJdbc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestJdbcService {
	
	public void basicJdbc() throws ClassNotFoundException, SQLException {
		log.info("## basicJdbc");
		
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
		
		List<TestJdbc> list = new ArrayList<>();
		while (rs.next()) {
			int idx = rs.getInt("idx");
			int num = rs.getInt("num");
			list.add(new TestJdbc(idx, num));
		}
		log.info("\t > {}", list);
		
		rs.close();
		stmt.close();
		conn.close();
	}
}
