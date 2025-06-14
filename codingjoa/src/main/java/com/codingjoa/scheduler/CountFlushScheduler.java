package com.codingjoa.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.RedisKeyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CountFlushScheduler {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final RedisService redisService;
	private final BoardService boardService;
	private final CommentService commentService;
	
	//@Scheduled(fixedRate = 30000)
	//@Scheduled(cron = "0 0/1 * * * ?")
	public void flushCommentCount() {
		log.info("## flushCommentCount, performed at: {}", LocalDateTime.now().format(formatter));
		Set<String> keys = redisService.keys("board:*:comment_count");
		if (keys.isEmpty()) {
			return;
		}
		
		for (String key: keys) {
			int countDelta = redisService.getDelta(key);
			if (countDelta == 0) {
				continue;
			}
			
			Long boardId = RedisKeyUtils.extractEntityId(key);
			boardService.applyCommentCountDelta(countDelta, boardId);
			
			redisService.delete(key);
			log.info("\t > flushed countDelta: {}, boardId: {}", countDelta, boardId);
		}
	}
	
	//@Scheduled(cron = "0 0/1 * * * ?")
	public void flushBoardLikeCount() {
		log.info("## flushBoardLikeCount, performed at: {}", LocalDateTime.now().format(formatter));
		Set<String> keys = redisService.keys("board:*:like_count");
		if (keys.isEmpty()) {
			return;
		}
		
		for (String key: keys) {
			int countDelta = redisService.getDelta(key);
			if (countDelta == 0) {
				continue;
			}
			
			Long boardId = RedisKeyUtils.extractEntityId(key);
			boardService.applyLikeCountDelta(countDelta, boardId);
			
			redisService.delete(key);
			log.info("\t > flushed countDelta: {}, boardId: {}", countDelta, boardId);
		}
	}
	
	//@Scheduled(cron = "0 0/1 * * * ?")
	public void flushCommentLikeCount() {
		log.info("## flushCommentLikeCount, performed at: {}", LocalDateTime.now().format(formatter));
		Set<String> keys = redisService.keys("comment:*:like_count");
		if (keys.isEmpty()) {
			return;
		}
		
		for (String key : keys) {
			int countDelta = redisService.getDelta(key);
			if (countDelta == 0) {
				continue;
			}
			
			Long commentId = RedisKeyUtils.extractEntityId(key);
			commentService.applyLikeCountDelta(countDelta, commentId);
			
			redisService.delete(key);
			log.info("\t > flushed countDelta: {}, commentId: {}", countDelta, commentId);
		}
	}
	
	
}
