package com.codingjoa.service;

public interface RedisService {
	
	void save(String key, String authCode);
	
	boolean hasKey(String key);
	
	String findValueByKey(String key);
	
	boolean isAuthCodeValid(String key, String authCode);
	
	void deleteKey(String key);
}
