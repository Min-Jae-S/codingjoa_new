package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLikes;
import com.codingjoa.entity.CommentLikes;

@Mapper
public interface LikesMapper {
	
	BoardLikes findBoardLikes(@Param("boardIdx") int boardIdx, @Param("memberIdx") int memberIdx);
	
	void insertBoardLikes(@Param("boardIdx") int boardIdx, @Param("memberIdx") int memberIdx);
	
	void deleteBoardLikes(BoardLikes boardLikes);

	CommentLikes findCommentLikes(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void insertCommentLikes(@Param("commentIdx") int commentIdx, @Param("memberIdx") int memberIdx);
	
	void deleteCommentLikes(CommentLikes commentLikes);
	
	int findBoardLikesCnt(int boardIdx);

	int findCommentLikesCnt(int commentIdx);
}
