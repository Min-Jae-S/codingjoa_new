package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;

@Mapper
public interface LikesMapper {
	
	void mergeBoardLikes(BoardLikes boardLikes);
	
	void mergeCommentLikes(CommentLikes commentLikes);
}
