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
	private final CommentMapper commentMapper;
	
	@Override
	public boolean toggleBoardLike(long boardId, long userId) {
		log.info("\t > prior to toggling boardLike, find board first");
		Board board = boardMapper.findBoardById(boardId);
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		log.info("\t > to check whether the board is liked or not, find boardLike first");
		BoardLike boardLike = likeMapper.findBoardLike(boardId, userId);
		
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
	public boolean toggleCommentLike(long commentId, long userId) {
		log.info("\t > prior to toggling commentLike, find comment first");
		Comment comment = commentMapper.findCommentById(commentId);
		
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		log.info("\t > to check whether the comment is liked or not, find commentLike first");
		CommentLike commentLike = likeMapper.findCommentLike(commentId, userId);
		
		if (commentLike == null) {
			log.info("\t > insert commentLike");
			likeMapper.insertCommentLike(commentId, userId);
			return true;
		} else {
			log.info("\t > delete commentLike");
			likeMapper.deleteCommentLike(commentLike);
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
	public int getCommentLikeCnt(long commentId) {
		Comment comment = commentMapper.findCommentById(commentId);
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		return likeMapper.findCommentLikeCnt(commentId);
	}
}
