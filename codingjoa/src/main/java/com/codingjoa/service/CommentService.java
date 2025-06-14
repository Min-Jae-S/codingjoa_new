package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.entity.Comment;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;

public interface CommentService {

	List<CommentDetailsDto> getPagedComments(Long boardId, CommentCriteria commentCri, Long userId);
	
	Pagination getPagination(Long boardId, CommentCriteria commentCri);
	
	void saveComment(CommentDto commentDto);
	
	void updateComment(CommentDto commentDto);
	
	void deleteComment(Long commentId, Long userId);
	
	Comment getComment(Long commentId);
	
	void applyLikeCountDelta(Integer countDelta, Long commentId);
}
