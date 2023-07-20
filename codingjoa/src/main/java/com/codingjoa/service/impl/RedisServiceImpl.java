package com.codingjoa.service.impl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.codingjoa.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void save(String key, String value) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value, Duration.ofMinutes(5L));
	}

	@Override
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	@Override
	public String get(String key) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations.get(key);
	}
	
	@Override
	public boolean isAuthCodeValid(String key, String value) {
		return value.equals(get(key));
	}

	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

}
