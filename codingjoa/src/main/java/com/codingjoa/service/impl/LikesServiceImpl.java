package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardLikesDto;
import com.codingjoa.dto.CommentLikesDto;
import com.codingjoa.entity.Board;
import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.BoardMapper;
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
	private BoardMapper boardMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void toggleBoardLikes(BoardLikesDto boardLikesDto) {
		
		Board board = boardMapper.findBoardByIdx(boardLikesDto.getBoardIdx());
		log.info("\t find board = {}", board);
		
		if (board == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundBoard"));
		}
		
		BoardLikes boardLikes = modelMapper.map(boardLikesDto, BoardLikes.class);
		log.info("\t > boardLikesDto ==> {}", boardLikes);
		
		likesMapper.toggleBoardLikes(boardLikes);
		
	}

	@Override
	public void toggleCommentLikes(CommentLikesDto commentLikesDto) {
		CommentLikes commentLikes = modelMapper.map(commentLikesDto, CommentLikes.class);
		log.info("\t > commentLikesDto ==> {}", commentLikes);
		
		likesMapper.toggleCommentLikes(commentLikes);
		log.info("\t > DB commentIdx = {}", commentLikes.getCommentIdx());
		
		if (commentLikes.getCommentIdx() == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundComment"));
		}
		
	}
	
}
