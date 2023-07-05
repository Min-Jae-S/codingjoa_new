package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;

@Mapper
public interface LikesMapper {
	
	void delOrInsBoardLikes(BoardLikes boardLikes); // mergeBoardLikes
	
	void delOrInsCommentLikes(CommentLikes commentLikes); // mergeCommentLikes
	
	int findBoardLikesCnt(int boardIdx);

	int findCommentLikesCnt(int boardIdx);
}
