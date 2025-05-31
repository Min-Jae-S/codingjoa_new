package com.codingjoa.util;

import java.sql.Connection;
import java.util.Map;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionUtils {

	private TransactionUtils() {}
	
	public static boolean isTransactionAcitve() {
		return TransactionSynchronizationManager.isActualTransactionActive();
	}
	
	public static String getCurrentTransactionName() {
		return TransactionSynchronizationManager.getCurrentTransactionName();
	}
	
	public static Integer getTranscationHash() {
		Integer transacionHash = null;
		Map<Object, Object> resources = TransactionSynchronizationManager.getResourceMap();
		for (Object key : resources.keySet()) {
			Object resource = TransactionSynchronizationManager.getResource(key);
			if (resource instanceof ConnectionHolder) {
				ConnectionHolder holder = (ConnectionHolder) resource;
				Connection conn = holder.getConnection();
				transacionHash = System.identityHashCode(conn);
				break;
			}
		}
		
		return transacionHash;
	}
	
}
