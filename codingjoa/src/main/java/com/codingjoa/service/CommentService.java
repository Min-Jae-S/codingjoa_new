package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;

public interface CommentService {

	void saveComment(CommentDto commentDto);
	
	List<CommentDetailsDto> getPagedComment(Integer boardIdx, CommentCriteria commentCri, Integer memberIdx);
	
	Pagination getPagination(Integer boardIdx, CommentCriteria commentCri);
	
	CommentDetailsDto getModifyComment(Integer commentIdx, Integer memberIdx);
	
	void updateComment(CommentDto commentDto);
	
	void deleteComment(Integer commentIdx, Integer memberIdx);
}
