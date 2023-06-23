package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Comment;
import com.codingjoa.pagination.CommentCriteria;

@Mapper
public interface CommentMapper {
	
	void insertComment(Comment comment);

	List<Map<String, Object>> findPagedComment(@Param("boardIdx") int boardIdx,
			@Param("commentCri") CommentCriteria commentCri);
	
	int findPagedCommentTotalCnt(@Param("boardIdx") int boardIdx,
			@Param("commentCri") CommentCriteria commentCri);
	
	Map<String, Object> findCommentDetails(int commentIdx);
	
	void updateComment(Comment comment);
	
	void deleteComment(Comment comment);
}
