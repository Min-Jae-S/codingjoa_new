package com.codingjoa.service;

public interface RedisService {
	
	void saveKeyAndValue(String key, String value);
	
	boolean hasKey(String key);
	
	String findValueByKey(String key);
	
	boolean isAuthCodeValid(String key, String value);
	
	void deleteKey(String key);
}
