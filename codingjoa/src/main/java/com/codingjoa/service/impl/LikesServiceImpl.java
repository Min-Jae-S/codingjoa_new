package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardLikesDto;
import com.codingjoa.entity.Board;
import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.Comment;
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
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Integer toggleBoardLikes(BoardLikesDto boardLikesDto) {
		BoardLikes boardLikes = modelMapper.map(boardLikesDto, BoardLikes.class);
		log.info("\t > boardLikesDto ==> {}", boardLikes);
		
		likesMapper.delOrInsBoardLikes(boardLikes);
		log.info("\t > dbBoardIdx = {}", boardLikes.getBoardIdx());
		log.info("\t > dbBoardLikesIdx = {}", boardLikes.getBoardLikesIdx());
		
		if (boardLikes.getBoardIdx() == null) {
			throw new ExpectedException("error.NotFoundBoard");
		}
		
		return boardLikes.getBoardLikesIdx();
	}
	
	@Override
	public boolean toggleCommentLikes(int commentIdx, int memberIdx) {
		Comment comment = commentMapper.findCommentByIdx(commentIdx);
		log.info("\t > prior to toggling commentLikes, find comment");
		
		if (comment == null) {
			throw new ExpectedException("error.NotFoundComment");
		}
		
		boolean isCommentLiked = likesMapper.isCommentLiked(commentIdx, memberIdx);
		if (isCommentLiked) {
			log.info("\t > delete commentLikes");
			likesMapper.deleteCommentLikes(commentIdx, memberIdx);
		} else {
			log.info("\t > insert commentLikes");
			likesMapper.insertCommentLikes(commentIdx, memberIdx);
		}
		
		return !isCommentLiked;
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
