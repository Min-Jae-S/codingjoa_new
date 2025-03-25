package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardLike;
import com.codingjoa.entity.CommentLike;

@Mapper
public interface LikeMapper {
	
	BoardLike findBoardLike(@Param("boardId") Long boardId, @Param("userId") Long userId);
	
	void insertBoardLike(@Param("boardId") Long boardId, @Param("userId") Long userId);
	
	void deleteBoardLike(Long boardLikeId);

	CommentLike findCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);
	
	void insertCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);
	
	void deleteCommentLike(Long commentLikeId);
	
	int findBoardLikeCnt(Long boardId);

	int findCommentLikeCnt(Long commentId);
}
