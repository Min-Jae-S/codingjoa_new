package com.codingjoa.service;

public interface RedisService {
	
	void saveAuthCode(String key, String authCode);
	
	boolean hasAuthCode(String key);
	
	boolean isAuthCodeValid(String key, String authCode);
	
	void delete(String key);
}
