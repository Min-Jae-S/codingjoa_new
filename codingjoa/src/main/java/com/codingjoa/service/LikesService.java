package com.codingjoa.service;

import com.codingjoa.dto.BoardLikesDto;

public interface LikesService {

	Integer toggleBoardLikes(BoardLikesDto boardLikesDto);
	
	boolean toggleCommentLikes(int commentIdx, int memberIdx);
	
	int getBoardLikesCnt(int boardIdx);

	int getCommentLikesCnt(int commentIdx);
}
