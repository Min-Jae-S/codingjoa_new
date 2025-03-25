package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Comment;
import com.codingjoa.pagination.CommentCriteria;

@Mapper
public interface CommentMapper {
	
	boolean insertComment(Comment comment);

	List<Map<String, Object>> findPagedComments(@Param("boardId") Long boardId, 
			@Param("commentCri") CommentCriteria commentCri, @Param("userId") Long userId);
	
	int findTotalCntForPaging(Long boardId);
	
	int findValidCntForPaging(Long boardId);
	
	Comment findCommentById(Long commentId);
	
	boolean updateComment(Comment comment);
	
	boolean deleteComment(Comment comment);
	
	void increaseLikeCount(Long commentId);
	
	void decreaseLikeCount(Long commentId);
	
}
