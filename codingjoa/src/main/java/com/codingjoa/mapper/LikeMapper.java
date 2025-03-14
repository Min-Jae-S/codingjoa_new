package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.CommentLike;

@Mapper
public interface LikeMapper {
	
	BoardLike findBoardLike(@Param("boardId") Long boardId, @Param("userId") Long userId);
	
	void insertBoardLike(@Param("boardId") Long boardId, @Param("userId") Long userId);
	
	void deleteBoardLike(BoardLike boardLike);

	CommentLike findReplyLike(@Param("replyId") Long replyId, @Param("userId") Long userId);
	
	void insertReplyLike(@Param("replyId") Long replyId, @Param("userId") Long userId);
	
	void deleteReplyLike(CommentLike replyLike);
	
	int findBoardLikeCnt(Long boardId);

	int findReplyLikeCnt(Long replyId);
}
