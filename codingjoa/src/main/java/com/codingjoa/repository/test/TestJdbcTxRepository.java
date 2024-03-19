package com.codingjoa.repository.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
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
	
	private void close(PreparedStatement pstmt) {
		try {
			if (pstmt != null) pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveItem(Connection conn, int num) {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO test3 (idx, num) VALUES (seq_test3.NEXTVAL, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
	}
	
	public void saveItem(int num) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		log.info("\t > conn from repository = {}", conn);
		
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO test3 (idx, num) VALUES (seq_test3.NEXTVAL, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
	}
}
