package com.codingjoa.repository.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.codingjoa.test.TestItem;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Repository
public class TestJdbcTxRepository {

	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;
	
	public TestJdbcTxRepository(@Qualifier("mainDataSource") DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void saveItem(Connection conn, int num) {
		String sql = "INSERT INTO test3 (idx, num) VALUES (seq_test3.NEXTVAL, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.info("\t > {}", e.getClass().getSimpleName());
			}
		}
	}
	
}
