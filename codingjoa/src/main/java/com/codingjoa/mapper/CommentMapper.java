package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Reply;
import com.codingjoa.pagination.CommentCriteria;

@Mapper
public interface CommentMapper {
	
	boolean insertComment(Reply comment);

	List<Map<String, Object>> findPagedComment(@Param("boardIdx") int boardIdx,
			@Param("commentCri") CommentCriteria commentCri, @Param("memberIdx") Integer memberIdx);
	
	int findCommentTotalCnt(int boardIdx);
	
	Reply findCommentByIdx(int commentIdx);
	
	boolean updateComment(Reply comment);
	
	boolean deleteComment(Reply comment);
	
}
