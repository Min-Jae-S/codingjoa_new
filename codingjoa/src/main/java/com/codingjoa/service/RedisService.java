package com.codingjoa.service;

public interface RedisService {
	
	void saveKeyAndValue(String key, Object value);
	
	boolean hasKey(String key);
	
	Object findValueByKey(String key);
	
	void deleteKey(String key);
}
