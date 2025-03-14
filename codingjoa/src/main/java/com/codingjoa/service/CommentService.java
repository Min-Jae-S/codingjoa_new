package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;

public interface CommentService {

	void saveComment(CommentDto commentDto);
	
	List<CommentDetailsDto> getPagedComments(long boardId, CommentCriteria commentCri, long userId);
	
	Pagination getPagination(long boardId, CommentCriteria commentCri);
	
	void updateComment(CommentDto commentDto);
	
	void deleteComment(long commentId, long userId);
	
}
