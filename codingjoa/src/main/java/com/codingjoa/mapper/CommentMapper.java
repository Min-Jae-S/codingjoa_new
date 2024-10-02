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

	List<Map<String, Object>> findPagedComment(@Param("boardIdx") Integer boardIdx, @Param("commentCri") CommentCriteria commentCri);
	
	int findCommentTotalCnt(int boardIdx);
	
	Comment findCommentByIdx(int commentIdx);
	
	boolean updateComment(Comment comment);
	
	boolean deleteComment(Comment comment);
}
