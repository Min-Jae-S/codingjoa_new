package com.codingjoa.service;

import com.codingjoa.dto.BoardLikesDto;
import com.codingjoa.dto.CommentLikesDto;

public interface LikesService {

	Integer toggleBoardLikes(BoardLikesDto boardLikesDto);
	
	Integer toggleCommentLikes(CommentLikesDto commentLikesDto);
	
	int getBoardLikesCnt(int boardIdx);

	int getCommentLikesCnt(int commentIdx);
}
