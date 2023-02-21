package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.ReplyDetailsDto;
import com.codingjoa.mapper.ReplyMapper;
import com.codingjoa.service.ReplyService;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<ReplyDetailsDto> getPagedReply() {
		return replyMapper.findPagedReply().stream()
				.map(replyDetailsMap -> modelMapper.map(replyDetailsMap, ReplyDetailsDto.class))
				.collect(Collectors.toList());
	}
	
	
}
