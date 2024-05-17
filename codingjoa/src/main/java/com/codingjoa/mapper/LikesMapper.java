package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLikes;

@Mapper
public interface LikesMapper {
	
	void delOrInsBoardLikes(BoardLikes boardLikes);
	
	//void mergeCommentLikes(CommentLikes commentLikes);
	
	boolean isCommentLiked(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void insertCommentLikes(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void deleteCommentLikes(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	int findBoardLikesCnt(int boardIdx);

	int findCommentLikesCnt(int commentIdx);
}
