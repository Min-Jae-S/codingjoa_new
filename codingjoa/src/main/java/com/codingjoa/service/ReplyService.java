package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.ReplyDetailsDto;
import com.codingjoa.dto.ReplyDto;
import com.codingjoa.pagination.ReplyCriteria;
import com.codingjoa.pagination.Pagination;

public interface ReplyService {

	void saveReply(ReplyDto replyDto);
	
	List<ReplyDetailsDto> getPagedReplies(long boardId, ReplyCriteria replyCri, long userId);
	
	Pagination getPagination(long boardId, ReplyCriteria replyCri);
	
	void updateReply(ReplyDto replyDto);
	
	void deleteReply(long replyId, long userId);
	
}
