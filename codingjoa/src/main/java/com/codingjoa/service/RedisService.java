package com.codingjoa.service;

import java.time.Duration;

public interface RedisService {
	
	void save(String key, Object value);

	void save(String key, Object value, Duration expiration);
	
	boolean hasKey(String key);
	
	Object get(String key);
	
	void delete(String key);
	
	void adjustCount(String key, long delta);
	
}
