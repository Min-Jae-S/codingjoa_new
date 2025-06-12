package com.codingjoa.service;

import java.time.Duration;
import java.util.Set;

public interface RedisService {
	
	void save(String key, Object value);

	void save(String key, Object value, Duration expiration);
	
	boolean hasKey(String key);
	
	Object get(String key);
	
	void delete(String key);
	
	void applyDelta(String key, long delta);
	
	Set<String> keys(String pattern);
	
}
