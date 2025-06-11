package com.codingjoa.service;

public interface RedisService {
	
	void save(String key, Object value);
	
	boolean hasKey(String key);
	
	Object get(String key);
	
	void delete(String key);
	
}
