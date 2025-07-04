package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.CommentLike;
import com.codingjoa.mapper.LikeMapper;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CommentService;
import com.codingjoa.service.LikeService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.RedisKeyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likeMapper;
	private final BoardService boardService;
	private final CommentService commentService;
	private final RedisService redisService;
	
	@Override
	public boolean toggleBoardLike(Long boardId, Long userId) {
		log.info("\t > prior to toggling boardLike, validating existence of board");
		boardService.getBoard(boardId);
	
		log.info("\t > to check whether the board is liked or not, find boardLike");
		BoardLike boardLike = likeMapper.findBoardLike(boardId, userId);
		String key = RedisKeyUtils.createBoardLikeCountKey(boardId);
		
		if (boardLike == null) {
			log.info("\t > insert boardLike");
			likeMapper.insertBoardLike(boardId, userId);
			redisService.applyDelta(key, 1);
			//boardMapper.increaseLikeCount(boardId);
			return true;
		} else {
			log.info("\t > delete boardLike");
			likeMapper.deleteBoardLike(boardLike.getId());
			redisService.applyDelta(key, -1);
			//boardMapper.decreaseLikeCount(boardId);
			return false;
		}
	}
	
	@Override
	public boolean toggleCommentLike(Long commentId, Long userId) {
		log.info("\t > prior to toggling commentLike, validating existence of comment");
		commentService.getComment(commentId);
	
		log.info("\t > to check whether the comment is liked or not, find commentLike");
		CommentLike commentLike = likeMapper.findCommentLike(commentId, userId);
		String key = RedisKeyUtils.createCommentLikeCountKey(commentId);
		
		if (commentLike == null) {
			log.info("\t > insert commentLike");
			likeMapper.insertCommentLike(commentId, userId);
			redisService.applyDelta(key, 1);
			return true;
		} else {
			log.info("\t > delete commentLike");
			likeMapper.deleteCommentLike(commentLike.getId());
			redisService.applyDelta(key, -1);
			return false;
		}
	}
	
}
