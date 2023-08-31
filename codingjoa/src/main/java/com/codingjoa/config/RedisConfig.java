package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("/WEB-INF/properties/redis.properties")
public class RedisConfig {
	
	@Value("${redis.host}")
	private String host;

	@Value("${redis.port}")
	private int port;
	
	@PostConstruct
	public void init() {
		log.info("[ RedisConfig initialized ]");
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
