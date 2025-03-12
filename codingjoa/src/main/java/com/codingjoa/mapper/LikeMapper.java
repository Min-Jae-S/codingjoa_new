package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.ReplyLike;

@Mapper
public interface LikeMapper {
	
	BoardLike findBoardLikes(@Param("boardIdx") int boardIdx, @Param("memberIdx") int memberIdx);
	
	void insertBoardLikes(@Param("boardIdx") int boardIdx, @Param("memberIdx") int memberIdx);
	
	void deleteBoardLikes(BoardLike boardLikes);

	ReplyLike findCommentLikes(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void insertCommentLikes(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void deleteCommentLikes(ReplyLike commentLikes);
	
	int findBoardLikesCnt(int boardIdx);

	int findCommentLikesCnt(int commentIdx);
}
