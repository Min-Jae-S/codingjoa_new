package com.codingjoa.scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final RedisService redisService;
	private final BoardService boardService;
	private final CommentService commentService;
	
	@Scheduled(cron = "0 0/1 * * * ?")
	//@Scheduled(fixedRate = 30000)
	public void flushCommentCount() {
		log.info("## flushCommentCount, executed at: {}", LocalDateTime.now().format(formatter));
		
		Set<String> keys = redisService.keys("board:*:comment_count");
		if (keys == null || keys.isEmpty()) {
			return;
		}
		
		for (String key: keys) {
			Integer countDelta = (Integer) redisService.get(key);
			if (countDelta == null || countDelta == 0) {
				continue;
			}
			
			Long boardId = extractEntityId(key);
			boardService.applyCommentCountDelta(countDelta, boardId);
			
			redisService.delete(key);
			log.info("\t > flushed '{}', countDelta: {}, boardId: {}", countDelta, boardId);
		}
		
	}
	
	public void flushBoardLikeCount() {
		log.info("## flushBoardLikeCount, executed at: {}", LocalDateTime.now().format(formatter));
	}
	
	public void flushCommentLikeCount() {
		log.info("## flushCommentLikeCount, executed at: {}", LocalDateTime.now().format(formatter));
	}
	
	private Long extractEntityId(String key) {
		String[] parts = key.split(":"); // "board:123:comment_count" --> [ "board", "123", "comment_count" ]
		return Long.parseLong(parts[1]);
	}
	
}
