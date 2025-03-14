package com.codingjoa.service;

public interface LikeService {

	boolean toggleBoardLike(long boardId, long userId);
	
	boolean toggleCommentLike(long commentId, long userId);
	
	int getBoardLikeCnt(long boardId);

	int getCommentLikeCnt(long commentId);
}
