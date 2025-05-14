package com.codingjoa.util;

import java.sql.Connection;
import java.util.Map;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionUtils {

	private TransactionUtils() {}
	
	public static void logTransaction() {
		boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
		Map<Object, Object> resources = TransactionSynchronizationManager.getResourceMap();
		for (Object key : resources.keySet()) {
			Object resource = TransactionSynchronizationManager.getResource(key);
			if (resource instanceof ConnectionHolder) {
				ConnectionHolder holder = (ConnectionHolder) resource;
				Connection conn = holder.getConnection();
				log.info("[TX] active: {}, connection hash: {}", isActive, System.identityHashCode(conn));
			}
		}
		
		
	}
	
}
