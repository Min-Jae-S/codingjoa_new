package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;

import com.codingjoa.service.CountService;
import com.codingjoa.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CountServiceImpl implements CountService {
	
	private static final String COMMENT_COUNT_KEY = "board:%d:comment_count";
	private final RedisService redisService;
	
	@Override
	public void incrementCommentCount(Long boardId) {
		log.info("## CountService.incrementCommentCount");
		String key = String.format(COMMENT_COUNT_KEY, boardId);
		redisService.applyDelta(key, 1);
	}

}
