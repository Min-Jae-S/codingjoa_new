package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.Comment;
import com.codingjoa.entity.CommentLike;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.mapper.LikeMapper;
import com.codingjoa.service.LikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likeMapper;
	private final BoardMapper boardMapper;
	private final CommentMapper replyMapper;
	
	@Override
	public boolean toggleBoardLike(long boardId, long userId) {
		Board board = boardMapper.findBoardById(boardId);
		log.info("\t > prior to toggling boardLike, find board first");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		BoardLike boardLike = likeMapper.findBoardLike(boardId, userId);
		log.info("\t > to check whether the board is liked or not, find boardLike first");
		
		if (boardLike == null) {
			log.info("\t > insert boardLike");
			likeMapper.insertBoardLike(boardId, userId);
			return true;
		} else {
			log.info("\t > delete boardLike");
			likeMapper.deleteBoardLike(boardLike);
			return false;
		}
	}
	
	@Override
	public boolean toggleReplyLike(long replyId, long userId) {
		Comment reply = replyMapper.findReplyById(replyId);
		log.info("\t > prior to toggling replyLike, find reply first");
		
		if (reply == null) {
			throw new ExpectedException("error.NotFoundReply");
		}
		
		CommentLike replyLike = likeMapper.findReplyLike(replyId, userId);
		log.info("\t > to check whether the reply is liked or not, find replyLike first");
		
		if (replyLike == null) {
			log.info("\t > insert replyLike");
			likeMapper.insertReplyLike(replyId, userId);
			return true;
		} else {
			log.info("\t > delete replyLike");
			likeMapper.deleteReplyLike(replyLike);
			return false;
		}
	}

	@Override
	public int getBoardLikeCnt(long boardId) {
		Board board = boardMapper.findBoardById(boardId);
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return likeMapper.findBoardLikeCnt(boardId);
	}

	@Override
	public int getReplyLikeCnt(long replyId) {
		Comment reply = replyMapper.findReplyById(replyId);
		if (reply == null) {
			throw new ExpectedException("error.NotFoundReply");
		}
		
		return likeMapper.findReplyLikeCnt(replyId);
	}
}
