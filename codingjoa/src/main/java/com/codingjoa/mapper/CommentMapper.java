package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Comment;
import com.codingjoa.pagination.CommentCriteria;

@Mapper
public interface CommentMapper {
	
	void insertComment(Comment comment);

	List<Map<String, Object>> findPagedComment(CommentCriteria commentCri);
	
	Map<String, Object> findCommentDetails(int commentIdx);
	
	void deleteComment(int commentIdx);
}
