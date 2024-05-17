package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;

@Mapper
public interface LikesMapper {
	
	void delOrInsBoardLikes(BoardLikes boardLikes); 	// mergeBoardLikes
	
	//void mergeCommentLikes(CommentLikes commentLikes);
	
	CommentLikes findCommentLikesByCommentAndMember(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void insertCommentLikes(CommentLikes commentLikes);
	
	void deleteCommentLikes(CommentLikes commentLikes);
	
	CommentLikes findCommentLikesByIdx(int commentLikesIdx);
	
	int findBoardLikesCnt(int boardIdx);

	int findCommentLikesCnt(int commentIdx);
}
