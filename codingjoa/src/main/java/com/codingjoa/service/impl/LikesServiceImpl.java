package com.codingjoa.service.impl;

import java.util.Map;

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
	public void toggleBoardLikes(BoardLikesDto boardLikesDto) {
		BoardLikes boardLikes = modelMapper.map(boardLikesDto, BoardLikes.class);
		log.info("\t > boardLikesDto ==> {}", boardLikes);
		
		likesMapper.mergeBoardLikes(boardLikes);
		log.info("\t > DB boardIdx = {}", boardLikes.getBoardIdx());
		
		if (boardLikes.getBoardIdx() == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundBoard"));
		}
	}
	
	@Override
	public BoardLikesDto getBoardLikes(Integer boardIdx, Integer memberIdx) {
		Map<String, Object> boardLikesMap = likesMapper.findBoardLikes(boardIdx, memberIdx);
		return modelMapper.map(boardLikesMap, BoardLikesDto.class);
	}

	@Override
	public void toggleCommentLikes(CommentLikesDto commentLikesDto) {
		CommentLikes commentLikes = modelMapper.map(commentLikesDto, CommentLikes.class);
		log.info("\t > commentLikesDto ==> {}", commentLikes);
		
		likesMapper.mergeCommentLikes(commentLikes);
		log.info("\t > DB commentIdx = {}", commentLikes.getCommentIdx());
		
		if (commentLikes.getCommentIdx() == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundComment"));
		}
	}

	@Override
	public boolean isBoardLikes(Integer boardIdx, Integer memberIdx) {
		return likesMapper.isBoardLikes(boardIdx, memberIdx);
	}

	
	
}
