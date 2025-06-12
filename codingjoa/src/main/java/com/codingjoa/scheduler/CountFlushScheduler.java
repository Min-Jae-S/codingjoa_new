package com.codingjoa.scheduler;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.codingjoa.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Component
public class CountFlushScheduler {

	private final RedisService redisService;
	private final BoardService boardSerivce;
	private final CommentService commentService;
	
	@Scheduled(cron = "0 0/1 * * * ?")
	public void flushCommentCount() {
		log.info("## flushCommentCount, executed at: {}", LocalDateTime.now());
		
		Set<String> keys = redisService.keys("board:*:comment_count");
		if (keys == null || keys.isEmpty()) {
			log.info("\t > no comment count for flush");
			return;
		}
		
		for (String key: keys) {
			Integer countDelta = (Integer) redisService.get(key);
			if (countDelta == null || countDelta == 0) {
				continue;
			}
			
			Long boardId = extractBoardId(key);
			boardSerivce.applyCommentCountDelta(countDelta, boardId);
			
			redisService.delete(key);
			log.info("\t > flushed '{}', countDelta: {}, boardId: {}", countDelta, boardId);
		}
		
	}
	
	private Long extractBoardId(String key) {
		String[] parts = key.split(":"); // "board:123:comment_count" --> [ "board", "123", "comment_count" ]
		return Long.parseLong(parts[1]);
	}
	
}
