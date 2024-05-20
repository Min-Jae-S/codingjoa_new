package com.codingjoa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.Comment;
import com.codingjoa.entity.CommentLikes;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.mapper.LikesMapper;
import com.codingjoa.service.LikesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class LikesServiceImpl implements LikesService {

	@Autowired
	private LikesMapper likesMapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Override
	public boolean toggleBoardLikes(int boardIdx, int memberIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		log.info("\t > prior to toggling boardLikes, find board");
		
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		BoardLikes boardLikes = likesMapper.findBoardLikes(boardIdx, memberIdx);
		log.info("\t > to check whether the board is liked or not, find boardLikes");
		log.info("\t > boardLikes = {}", boardLikes);
		
		if (boardLikes == null) {
			log.info("\t > insert boardLikes");
			likesMapper.insertBoardLikes(boardIdx, memberIdx);
			return true;
		} else {
			log.info("\t > delete boardLikes");
			likesMapper.deleteBoardLikes(boardLikes.getBoardLikesIdx());
			return false;
		}
	}
	
	@Override
	public boolean toggleCommentLikes(int commentIdx, int memberIdx) {
		Comment comment = commentMapper.findCommentByIdx(commentIdx);
		log.info("\t > prior to toggling commentLikes, find comment");
		
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		CommentLikes commentLikes = likesMapper.findCommentLikes(commentIdx, memberIdx);
		log.info("\t > to check whether the comment is liked or not, find commentLikes");
		log.info("\t > commentLikes = {}", commentLikes);
		
		if (commentLikes == null) {
			log.info("\t > insert commentLikes");
			likesMapper.insertCommentLikes(commentIdx, memberIdx);
			return true;
		} else {
			log.info("\t > delete commentLikes");
			likesMapper.deleteCommentLikes(commentLikes.getCommentLikesIdx());
			return false;
		}
	}

	@Override
	public int getBoardLikesCnt(int boardIdx) {
		Board board = boardMapper.findBoardByIdx(boardIdx);
		if (board == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return likesMapper.findBoardLikesCnt(boardIdx);
	}

	@Override
	public int getCommentLikesCnt(int commentIdx) {
		Comment comment = commentMapper.findCommentByIdx(commentIdx);
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		return likesMapper.findCommentLikesCnt(commentIdx);
	}
}
