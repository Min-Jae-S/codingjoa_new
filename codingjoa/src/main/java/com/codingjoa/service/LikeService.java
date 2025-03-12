package com.codingjoa.service;

public interface LikeService {

	boolean toggleBoardLike(long boardId, long userId);
	
	boolean toggleReplyLike(long replyId, long userId);
	
	int getBoardLikeCnt(long boardId);

	int getReplyLikeCnt(long replyId);
}
