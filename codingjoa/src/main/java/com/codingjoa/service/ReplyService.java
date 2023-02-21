package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.ReplyDetailsDto;
import com.codingjoa.dto.ReplyDto;

public interface ReplyService {

	void writeReply(ReplyDto replyDto);
	
	List<ReplyDetailsDto> getPagedReply();
}
