package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;

@Mapper
public interface LikesMapper {
	
	void mergeBoardLikes(BoardLikes boardLikes);
	
	Map<String, Object> findBoardLikes(@Param("boardIdx") Integer boardIdx, @Param("memberIdx") Integer memberIdx);
	
	void mergeCommentLikes(CommentLikes commentLikes);
	
	boolean isBoardLikes(@Param("boardIdx") Integer boardIdx, @Param("memberIdx") Integer memberIdx);
}
