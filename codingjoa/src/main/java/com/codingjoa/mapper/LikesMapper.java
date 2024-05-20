package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;

@Mapper
public interface LikesMapper {
	
	void delOrInsBoardLikes(BoardLikes boardLikes);
	
	//void mergeCommentLikes(CommentLikes commentLikes);
	
	CommentLikes findCommentLikesByCommentAndMember(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void insertCommentLikes(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void deleteCommentLikes(int commentLikesIdx);
	
	int findBoardLikesCnt(int boardIdx);

	int findCommentLikesCnt(int commentIdx);
}
