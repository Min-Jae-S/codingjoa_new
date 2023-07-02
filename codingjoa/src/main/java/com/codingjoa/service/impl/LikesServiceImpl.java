package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardLikesDto;
import com.codingjoa.dto.CommentLikesDto;
import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.LikesMapper;
import com.codingjoa.service.LikesService;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class LikesServiceImpl implements LikesService {

	@Autowired
	private LikesMapper likesMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Integer toggleBoardLikes(BoardLikesDto boardLikesDto) {
		BoardLikes boardLikes = modelMapper.map(boardLikesDto, BoardLikes.class);
		log.info("\t > boardLikesDto ==> {}", boardLikes);
		
		likesMapper.delOrInsBoardLikes(boardLikes);
		log.info("\t > DB boardIdx = {}", boardLikes.getBoardIdx());
		log.info("\t > DB boardLikesIdx = {}", boardLikes.getBoardLikesIdx());
		
		if (boardLikes.getBoardIdx() == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundBoard"));
		}
		
		return boardLikes.getBoardLikesIdx();
	}
	
	@Override
	public Integer toggleCommentLikes(CommentLikesDto commentLikesDto) {
		CommentLikes commentLikes = modelMapper.map(commentLikesDto, CommentLikes.class);
		log.info("\t > commentLikesDto ==> {}", commentLikes);
		
		likesMapper.delOrInsCommentLikes(commentLikes);
		log.info("\t > DB commentIdx = {}", commentLikes.getCommentIdx());
		log.info("\t > DB commentLikesIdx = {}", commentLikes.getCommentLikesIdx());
		
		if (commentLikes.getCommentIdx() == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundComment"));
		}
		
		return commentLikes.getCommentLikesIdx();
	}

	@Override
	public boolean isBoardLikes(Integer boardIdx, Integer memberIdx) {
		return likesMapper.isBoardLikes(boardIdx, memberIdx);
	}

	@Override
	public boolean isCommentLikes(Integer commentIdx, Integer memberIdx) {
		return likesMapper.isCommentLikes(commentIdx, memberIdx);
	}
	
	@Override
	public int getBoardLikesCnt(Integer boardIdx) {
		int boardLikesCnt = likesMapper.findBoardLikesCnt(boardIdx);
		log.info("\t > boardLikesCnt = {}", boardLikesCnt);
		
		return boardLikesCnt;
	}

	@Override
	public int getCommentLikesCnt(Integer commentIdx) {
		int commentLikesCnt = likesMapper.findCommentLikesCnt(commentIdx);
		log.info("\t > commentLikesCnt = {}", commentLikesCnt);
		
		return commentLikesCnt;
	}
	
	
}
