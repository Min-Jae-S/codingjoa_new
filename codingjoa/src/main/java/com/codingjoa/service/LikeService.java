package com.codingjoa.service;

public interface LikeService {

	boolean toggleBoardLike(Long boardId, Long userId);
	
	boolean toggleCommentLike(Long commentId, Long userId);
	
//	int getBoardLikeCnt(Long boardId);
//
//	int getCommentLikeCnt(Long commentId);
}
