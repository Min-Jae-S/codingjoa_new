package com.codingjoa.controller.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.entity.Board;
import com.codingjoa.entity.Comment;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.RedisKeyUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import lombok.extern.slf4j.Slf4j;

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
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
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
		if (keys.isEmpty()) {
			log.info("\t > no keys");
		}
		
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
		
		redisService.save(key, 123L);
		log.info("\t > saved 123, value: {}", redisService.get(key));
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/redis/incr1")
	public ResponseEntity<Object> redisIncr1() {
		log.info("## redisIncr1");
		String key = "test:incr1:count";
		redisService.applyDelta(key, 1);
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
					redisService.applyDelta(key, 1);
				} catch (Exception e) {
					log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}
		
		latch.await();
		executor.shutdown();
		log.info("\t > final: {}", redisService.get(key));
		
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
	
	@GetMapping("/incr/comment_count/board/{boardId}")
	public ResponseEntity<Object> incrCommentCountByBoardId(@PathVariable Long boardId) {
		log.info("## incrCommentCountByBoardId");
		String key = RedisKeyUtils.createCommentCountKey(boardId);
		redisService.applyDelta(key, 1);
		log.info("\t > after applying delta: {}", redisService.getDelta(key));
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/decr/comment_count/board/{boardId}")
	public ResponseEntity<Object> decrCommentCountByBoardId(@PathVariable Long boardId) {
		log.info("## decrCommentCountByBoardId");
		String key = RedisKeyUtils.createCommentCountKey(boardId);
		redisService.applyDelta(key, -1);
		log.info("\t > after applying delta: {}", redisService.getDelta(key));
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/flush/view_count")
	public ResponseEntity<Object> flushViewCount() {
		log.info("## flushViewCount");
		
		Set<String> keys = redisService.keys("board:*:view_count");
		for (String key: keys) {
			int countDelta = redisService.getDelta(key);
			log.info("\t > current delta: {}", countDelta);

			Long entityId = RedisKeyUtils.extractEntityId(key);
			Board initialBoard = boardService.getBoard(entityId);
			boardService.applyViewCountDelta(countDelta, entityId);
			redisService.delete(key);
			
			Board finalBoard = boardService.getBoard(entityId);
			log.info("\t\t - [initial] viewCount: {}", initialBoard.getViewCount());
			log.info("\t\t - [final]   viewCount: {}", finalBoard.getViewCount());
		}
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/flush/comment_count")
	public ResponseEntity<Object> flushCommentCount() {
		log.info("## flushCommentCount");
		
		Set<String> keys = redisService.keys("board:*:comment_count");
		for (String key: keys) {
			int countDelta = redisService.getDelta(key);
			log.info("\t > current delta: {}", countDelta);
			
			Long entityId = RedisKeyUtils.extractEntityId(key);
			Board initialBoard = boardService.getBoard(entityId);
			boardService.applyCommentCountDelta(countDelta, entityId);
			redisService.delete(key);
			
			Board finalBoard = boardService.getBoard(entityId);
			log.info("\t\t - [initial] commentCount: {}", initialBoard.getCommentCount());
			log.info("\t\t - [final]   commentCount: {}", finalBoard.getCommentCount());
		}
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/flush/board_like_count")
	public ResponseEntity<Object> flushBoardLikeCount() {
		log.info("## flushBoardLikeCount");
		
		Set<String> keys = redisService.keys("board:*:like_count");
		for (String key: keys) {
			int countDelta = redisService.getDelta(key);
			log.info("\t > current delta: {}", countDelta);
			
			Long entityId = RedisKeyUtils.extractEntityId(key);
			Board initialBoard = boardService.getBoard(entityId);
			boardService.applyLikeCountDelta(countDelta, entityId);
			redisService.delete(key);
			
			Board finalBoard = boardService.getBoard(entityId);
			log.info("\t\t - [initial] likeCount: {}", initialBoard.getLikeCount());
			log.info("\t\t - [final]   likeCount: {}", finalBoard.getLikeCount());
		}
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/flush/comment_like_count")
	public ResponseEntity<Object> flushCommentLikeCount() {
		log.info("## flushCommentLikeCount");
		
		Set<String> keys = redisService.keys("comment:*:like_count");
		for (String key: keys) {
			int countDelta = redisService.getDelta(key);
			log.info("\t > current delta: {}", countDelta);
			
			Long entityId = RedisKeyUtils.extractEntityId(key);
			Comment initialComment = commentService.getComment(entityId);
			commentService.applyLikeCountDelta(countDelta, entityId);
			redisService.delete(key);
			
			Comment finalComment = commentService.getComment(entityId);
			log.info("\t\t - [initial] likeCount: {}", initialComment.getLikeCount());
			log.info("\t\t - [final]   likeCount: {}", finalComment.getLikeCount());
		}
		
		return ResponseEntity.ok(SuccessResponse.create());

	}
	
	
}
