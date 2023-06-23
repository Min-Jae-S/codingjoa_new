package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;

public interface CommentService {

	void writeComment(CommentDto commentDto);
	
	List<CommentDetailsDto> getPagedComment(int commentBoardIdx, CommentCriteria commentCri);
	
	Pagination getPagination(int commentBoardIdx, CommentCriteria commentCri);
	
	CommentDetailsDto getCommentDetails(int commentIdx, int commentWriterIdx);
	
	void modifyComment(CommentDto commentDto);
	
	void deleteComment(CommentDto commentDto);
}
