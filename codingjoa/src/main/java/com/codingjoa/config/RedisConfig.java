package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RedisConfig {
	
	private final String host;
	private final int port;
	
	public RedisConfig(@Value("${redis.host}") String host, @Value("${redis.port}") int port) {
		this.host = host;
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		log.info("## RedisConfig");
		log.info("\t > host = {}", host);
		log.info("\t > port = {}", port);
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}
	
//	@Bean
//	public RedisSerializer<?> redisSerializer() {
//		return new StringRedisSerializer();
//	}
	
//	@Bean
//	public RedisTemplate<String, String> redisTemplate() {
//		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setDefaultSerializer(redisSerializer());
//
//        return redisTemplate;
//	}
	
	@Bean
	public RedisTemplate<?, ?> redistTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}
