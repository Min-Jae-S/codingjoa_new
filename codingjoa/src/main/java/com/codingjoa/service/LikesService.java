package com.codingjoa.service;

public interface LikesService {

	boolean toggleBoardLikes(int boardIdx, int memberIdx);
	
	boolean toggleCommentLikes(int commentIdx, int memberIdx);
	
	int getBoardLikesCnt(int boardIdx);

	int getCommentLikesCnt(int commentIdx);
}
