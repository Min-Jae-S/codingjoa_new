package com.codingjoa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.Comment;
import com.codingjoa.entity.CommentLike;
import com.codingjoa.error.ExpectedException;
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
		log.info("\t > prior to toggling boardLike, find board");
		Board board = boardMapper.findBoardById(boardId);
		
		if (board == null) {
			throw new ExpectedException("error.like.notFoundBoard");
		}
		
		log.info("\t > to check whether the board is liked or not, find boardLike");
		BoardLike boardLike = likeMapper.findBoardLike(boardId, userId);
		
		if (boardLike == null) {
			log.info("\t > insert boardLike");
			likeMapper.insertBoardLike(boardId, userId);
			boardMapper.increaseLikeCount(boardId);
			return true;
		} else {
			log.info("\t > delete boardLike");
			likeMapper.deleteBoardLike(boardLike.getId());
			boardMapper.decreaseLikeCount(boardId);
			return false;
		}
	}
	
	@Override
	public boolean toggleCommentLike(long commentId, long userId) {
		log.info("\t > prior to toggling commentLike, find comment");
		Comment comment = commentMapper.findCommentById(commentId);
		
		if (comment == null) {
			throw new ExpectedException("error.like.notFoundComment");
		}
		
		log.info("\t > to check whether the comment is liked or not, find commentLike");
		CommentLike commentLike = likeMapper.findCommentLike(commentId, userId);
		
		if (commentLike == null) {
			log.info("\t > insert commentLike");
			likeMapper.insertCommentLike(commentId, userId);
			commentMapper.increaseLikeCount(commentId);
			return true;
		} else {
			log.info("\t > delete commentLike");
			likeMapper.deleteCommentLike(commentLike.getId());
			commentMapper.decreaseLikeCount(commentId);
			return false;
		}
	}

	@Override
	public int getBoardLikeCnt(long boardId) {
		Board board = boardMapper.findBoardById(boardId);
		if (board == null) {
			throw new ExpectedException("error.like.notFoundBoard");
		}
		
		return likeMapper.findBoardLikeCnt(boardId);
	}

	@Override
	public int getCommentLikeCnt(long commentId) {
		Comment comment = commentMapper.findCommentById(commentId);
		if (comment == null) {
			throw new ExpectedException("error.like.notFoundComment");
		}
		
		return likeMapper.findCommentLikeCnt(commentId);
	}
}
