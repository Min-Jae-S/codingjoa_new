package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;

public interface CommentService {

	void saveReply(CommentDto replyDto);
	
	List<CommentDetailsDto> getPagedReplies(long boardId, CommentCriteria replyCri, long userId);
	
	Pagination getPagination(long boardId, CommentCriteria replyCri);
	
	void updateReply(CommentDto replyDto);
	
	void deleteReply(long replyId, long userId);
	
}
