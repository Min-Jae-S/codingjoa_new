package com.codingjoa.service.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.codingjoa.repository.test.TestJdbcTxRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestJdbcTxService {
	
	@Autowired(required = false)
	private TestJdbcTxRepository jdbcRepository;

	private final PlatformTransactionManager txManager;
	private final DataSource dataSource;
	
	public TestJdbcTxService(@Qualifier("mainDataSource") DataSource dataSource, 
			@Qualifier("mainTransactionManager") PlatformTransactionManager txManager) {
		this.dataSource = dataSource;
		this.txManager = txManager;
	}
	
	private void close(Connection conn) {
		try {
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void useTx(boolean commit) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			
			int num = RandomUtils.nextInt(1, 999);
			log.info("\t > num = {}", num);
			jdbcRepository.saveItem(conn, num);
			
			if (commit) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			close(conn);
		}
	}

	public void useTxSyncManager(boolean commit) {
		TransactionSynchronizationManager.initSynchronization();
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
			conn.setAutoCommit(false);
			
			int num = RandomUtils.nextInt(1, 999);
			log.info("\t > num = {}", num);
			jdbcRepository.saveItem(num);
			
			if (commit) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			close(conn);
		}
	}
	
	public void useTxManager(boolean commit) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);
		try {
			int num = RandomUtils.nextInt(1, 999);
			log.info("\t > num = {}", num);
			jdbcRepository.saveItem(num);
			
			if (commit) {
				txManager.commit(status);
			} else {
				txManager.rollback(status);
			}
		} catch (RuntimeException e) {
			txManager.rollback(status);
		}
	}
	
	@Transactional
	public void usetDeclarativeTx(boolean commit) {
		int num = RandomUtils.nextInt(1, 999);
		log.info("\t > num = {}", num);
		
		if (commit) {
			jdbcRepository.saveItem(num);
		} else {
			throw new RuntimeException("rollback");
		}
	}

}
