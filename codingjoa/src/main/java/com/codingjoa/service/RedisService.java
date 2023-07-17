package com.codingjoa.service;

public interface RedisService {
	
	void save(String key, String authCode);
	
	boolean hasKey(String key);
	
	String get(String key);
	
	boolean isAuthCodeValid(String key, String authCode);
	
	void delete(String key);
}
