package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.entity.Comment;
import com.codingjoa.mapper.CommentMapper;
import com.codingjoa.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper replyMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void writeReply(CommentDto replyDto) {
		Comment reply = modelMapper.map(replyDto, Comment.class);
		log.info("replyDto ==> {}", reply);
		
		replyMapper.insertReply(reply);
	}
	
	@Override
	public List<CommentDetailsDto> getPagedReply() {
		return replyMapper.findPagedReply().stream()
				.map(replyDetailsMap -> modelMapper.map(replyDetailsMap, CommentDetailsDto.class))
				.collect(Collectors.toList());
	}
	
	
}
