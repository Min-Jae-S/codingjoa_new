package com.codingjoa.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.entity.Comment;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.service.CommentService;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void writeComment(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
		log.info("commentDto ==> {}", comment);
		
		commentMapper.insertComment(comment);
	}
	
	@Override
	public List<CommentDetailsDto> getPagedComment(int boardIdx, CommentCriteria commentCri) {
		return commentMapper.findPagedComment(boardIdx, commentCri).stream()
				.map(commentDetailsMap -> modelMapper.map(commentDetailsMap, CommentDetailsDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public CommentDetailsDto getCommentDetails(int commentIdx, int commentWriterIdx) {
		Map<String, Object> commentDetailsMap = commentMapper.findCommentDetails(commentIdx, commentWriterIdx);
		if (commentDetailsMap == null) {
			throw new IllegalArgumentException(MessageUtils.getMessage("error.NotFoundComment"));
		}
		
		return modelMapper.map(commentDetailsMap, CommentDetailsDto.class);
	}
	
	@Override
	public void deleteComment(int commentIdx) {
		commentMapper.deleteComment(commentIdx);
	}
	
}
