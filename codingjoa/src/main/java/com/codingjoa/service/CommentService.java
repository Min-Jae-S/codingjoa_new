package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.pagination.CommentCriteria;

public interface CommentService {

	void writeComment(CommentDto commentDto);
	
	List<CommentDetailsDto> getPagedComment(int boardIdx, CommentCriteria commentCri);
	
	CommentDetailsDto getCommentDetails(int commentIdx);
	
	void deleteComment(int commentIdx);
}
