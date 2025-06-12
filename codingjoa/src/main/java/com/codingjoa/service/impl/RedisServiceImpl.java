package com.codingjoa.service.impl;

import java.time.Duration;
import java.util.Set;

import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.codingjoa.service.RedisService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {
	
	private static final Duration DEFAULT_EXPIRATION = Duration.ofMinutes(30L);
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void save(String key, Object value) {
		save(key, value, DEFAULT_EXPIRATION);
	}
	
	@Override
	public void save(String key, Object value, Duration expiration) {
		redisTemplate.opsForValue().set(key, value, expiration);
	}
	
	@Override
	public boolean hasKey(String key) {
		Boolean exists = redisTemplate.hasKey(key);
		if (exists == null) {
			throw new RedisSystemException("hasKey() returned unexpected null for key:" + key, null);
		}
		return exists;
	}
	
	@Override
	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	@Override
	public void delete(String key) {
		Boolean deleted = redisTemplate.delete(key);
		if (deleted == null) {
			throw new RedisSystemException("delete() returned unexpected null for key:" + key, null);
		}
	}

	@Override
	public void applyDelta(String key, long delta) {
		redisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

}
