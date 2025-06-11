package com.codingjoa.controller.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private RedisService redisService;
	
	@GetMapping("/hikari")
	public ResponseEntity<Object> hikariInfo() {
		log.info("## hikariInfo");
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
	public ResponseEntity<Object> redisInfo() {
		log.info("## redisInfo");
		log.info("\t > {}", redisTemplate.getClass().getSimpleName());
		log.info("\t\t - key serializer: {}", redisTemplate.getKeySerializer());
		log.info("\t\t - value serializer: {}", redisTemplate.getValueSerializer());
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/redis/init")
	public ResponseEntity<Object> initRedis() {
		log.info("## initRedis");
		redisTemplate.getConnectionFactory().getConnection().flushAll();
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/redis/entries")
	public ResponseEntity<Object> redisEntries() {
		log.info("## redisEntries");
		Map<String, Object> map = new HashMap<>();
		
		Set<String> keys = redisService.keys("*");
		log.info("\t > keys = {}", keys);
		
		for (String key : keys) {
			Object value = redisService.get(key);
			log.info("\t > key = {}, value = {} [{}]", key, value, value.getClass().getSimpleName());
			map.put(key, value);
		}
		return ResponseEntity.ok(SuccessResponse.builder().data(map).build());
	}
	
	@GetMapping("/redis/sample")
	public ResponseEntity<Object> redisSample() {
		log.info("## redisSample");
		String key = UUID.randomUUID().toString().replace("-", "");
		log.info("\t > key = {}", key);
		log.info("\t > initial value: {}", redisService.get(key));
		
		redisService.save(key, "ABC");
		log.info("\t > saved \"ABC\", value: {}",  redisService.get(key));
		
		redisService.save(key, 123);
		log.info("\t > saved 123, value: {}", redisService.get(key));
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/redis/incr")
	public ResponseEntity<Object> redisIncr() {
		log.info("## redisIncr");
		String key = "test:incr1:count";
		log.info("\t > initial: {}", redisService.get(key));
		
		for (int i = 0; i < 10; i++) {
			redisService.increment(key, 1);
		}
		log.info("\t > final: {}", redisService.get(key));
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/redis/incr2")
	public ResponseEntity<Object> redisIncr2() throws InterruptedException {
		log.info("## redisIncr2");
		String key = "test:incr2:count";
		redisService.delete(key);
		log.info("\t > initial: {}", redisService.get(key));
		
		int threadCount = 50;
		CountDownLatch latch = new CountDownLatch(threadCount);
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					log.info("\t > threadName: {}, threadId: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
					redisService.increment(key, 1);
				} catch (Exception e) {
					log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}
		
		latch.await();
		log.info("\t > final: {}", redisService.get(key));
		executor.shutdown();
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/redis/incr3")
	public ResponseEntity<Object> redisIncr3() {
		log.info("## redisIncr3");
		String key = "test:incr3:count";
		redisService.increment(key, 1);
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1: not CountDownLatch");
		log.info("\t > [start] threadName: {}, threadId: {}", Thread.currentThread().getName(), Thread.currentThread().getId());

		int threadCount = 5;
		
		for (int i = 1; i <= threadCount; i++) {
			new Thread(() -> {
				try {
					log.info("\t > [ing...] threadName: {}, threadId: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();
		}
		
		log.info("\t > [finish] threadName: {}, threadId: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/test2")
	public ResponseEntity<Object> test2() throws InterruptedException {
		log.info("## test2: CountDownLatch");
		log.info("\t > [start] threadName: {}, threadId: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
		
		int threadCount = 5;
		CountDownLatch latch = new CountDownLatch(threadCount);
		
		for (int i = 1; i <= threadCount; i++) {
			new Thread(() -> {
				try {
					log.info("\t > [ing...] threadName: {}, threadId: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					latch.countDown();
				}
			}).start();
		}
		
		latch.await();
		log.info("\t > [finish] threadName: {}, threadId: {}", Thread.currentThread().getName(), Thread.currentThread().getId());
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
}
