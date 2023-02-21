package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.ReplyDetailsDto;
import com.codingjoa.dto.ReplyDto;
import com.codingjoa.entity.Reply;
import com.codingjoa.mapper.ReplyMapper;
import com.codingjoa.service.ReplyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void writeReply(ReplyDto replyDto) {
		Reply reply = modelMapper.map(replyDto, Reply.class);
		log.info("replyDto ==> {}", reply);
		
		replyMapper.insertReply(reply);
	}
	
	@Override
	public List<ReplyDetailsDto> getPagedReply() {
		return replyMapper.findPagedReply().stream()
				.map(replyDetailsMap -> modelMapper.map(replyDetailsMap, ReplyDetailsDto.class))
				.collect(Collectors.toList());
	}
	
	
}
