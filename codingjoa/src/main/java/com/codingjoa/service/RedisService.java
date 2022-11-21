package com.codingjoa.service;

public interface RedisService {
	
	public void saveAuthCode(String key, String authCode);
	
	public boolean hasAuthCode(String key);
	
	public boolean isAuthCodeValid(String key, String authCode);
}
