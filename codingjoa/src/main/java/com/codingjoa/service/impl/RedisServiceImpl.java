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
	public void saveKeyAndValue(String key, Object value) {
		log.info("## saveKeyAndValue");
		log.info("\t > key = {}, value = {}", key, value);
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value, KEY_EXPIRATION);
	}

	@Override
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	@Override
	public Object findValueByKey(String key) {
		log.info("## findValueByKey, key = {}", key);
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		return valueOperations.get(key);
	}
	
	@Override
	public void deleteKey(String key) {
		redisTemplate.delete(key);
	}

}
