package com.codingjoa.controller.test;

import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.codingjoa.service.RedisService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequestMapping("/test/redis-concurrency")
@RestController
public class TestRedisConcurrencyController {

	@Qualifier("mainHikariConfig")
	@Autowired
	private HikariConfig hikariConfig;

	@Qualifier("mainDataSource")
	@Autowired
	private HikariDataSource hikariDataSource;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private RedisService redisService;
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		log.info("\t > conn timeout: {}", hikariConfig.getConnectionTimeout());
		log.info("\t > max pool size: {}", hikariConfig.getMaximumPoolSize());

		HikariPoolMXBean poolProxy = hikariDataSource.getHikariPoolMXBean();
		log.info("\t > HikariCP pool, total: {}, active: {}, idle: {}, wating: {}", 
				poolProxy.getTotalConnections(), poolProxy.getActiveConnections(), poolProxy.getIdleConnections(), poolProxy.getThreadsAwaitingConnection());
		
		Map<String, Object> data = Map.of(
			"total", poolProxy.getTotalConnections(),
			"active", poolProxy.getActiveConnections(),
			"idle", poolProxy.getIdleConnections(),
			"wating", poolProxy.getThreadsAwaitingConnection()
		);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@GetMapping("/redis")
	public ResponseEntity<Object> redisTest() {
		log.info("## redisTest");
		String key = UUID.randomUUID().toString();
		log.info("\t > key: {}", key);
		log.info("\t > initial value: {}", redisService.get(key));
		
		log.info("\t > save \"ABC\"");
		redisService.save(key, "ABC");
		log.info("\t\t - value: {}", redisService.get(key));

		log.info("\t > save 123");
		redisService.save(key, 123);
		log.info("\t\t - value: {}", redisService.get(key));
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/incr")
	public ResponseEntity<Object> incrTest() {
		log.info("## incrTest");
		Long boardId = 197481L;
		String key = String.format("board:%s:comment_delta", boardId);
		log.info("\t > initial: {} [ {} ]", redisService.get(key), key);
		
		log.info("\t > start adjust count (10 trials)");
		for (int i = 0; i < 10; i++) {
			redisService.adjustCount(key, 1);
		}
		
		log.info("\t > final: {} [ {} ]", redisService.get(key), key);
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	
	
	
}
