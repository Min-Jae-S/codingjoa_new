package com.codingjoa.service.impl;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.codingjoa.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {
	
	private static final Duration KEY_EXPIRATION = Duration.ofMinutes(30L);
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void save(String key, Object value) {
		log.info("## save redis key: {}, value: {}", key, value);
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		ops.set(key, value, KEY_EXPIRATION);
	}

	@Override
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	@Override
	public Object get(String key) {
		log.info("## find redis value by key: {}", key);
		return redisTemplate.opsForValue().get(key);
	}
	
	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

}
