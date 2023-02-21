package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.ReplyDetailsDto;

public interface ReplyService {

	List<ReplyDetailsDto> getPagedReply();
}
